/*
 * Copyright 2023 Blogger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.blogger.presentation.screens.dashboard.addpost_screen

import android.net.Uri
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.requests.PostBody
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.room.entities.DraftRecord
import com.peterchege.blogger.core.util.*
import com.peterchege.blogger.core.work.upload_post.UploadPostWorkManager
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.DraftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class AddPostFormState(
    val postTitle: String = "",
    val postBody: String = "",
    val uri: Uri? = null,
    val isLoading: Boolean = false,
    val isSaveDraftModalOpen: Boolean = false,
    val isFromDraft:Boolean = false,
    val draftId:Int? = null,

    )

@HiltViewModel
class AddPostScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val draftRepository: DraftRepository,
    private val uploadPostWorkManager: UploadPostWorkManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val isUploading = uploadPostWorkManager.isUploading
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = false
        )

    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5000L)
        )

    private val _formState = MutableStateFlow(AddPostFormState())
    val formState = _formState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("draftId")?.let {
            viewModelScope.launch {
                val draft = draftRepository.getDraftById(it)
                if(draft != null){
                    _formState.value = _formState.value.copy(
                        postTitle = draft.postTitle,
                        postBody = draft.postBody,
                        uri = if (draft.imageUri == "")
                            null else Uri.parse(draft.imageUri),
                        isFromDraft = true,
                        draftId = draft.id
                    )
                }
            }
        }
    }

    fun onCloseDialog(){
        _formState.value = _formState.value.copy(isSaveDraftModalOpen = false)
    }

    fun onBackPress(navigateBack: () -> Unit) {
        if (_formState.value.postTitle != "" || _formState.value.postBody != "") {
            _formState.value = _formState.value.copy(isSaveDraftModalOpen = true)
        } else {
            navigateBack()
        }
    }

    fun onChangePostTitle(text: String) {
        _formState.value = _formState.value.copy(postTitle = text)

    }

    fun onChangePostBody(text: String) {
        _formState.value = _formState.value.copy(postBody = text)
    }

    fun onChangePhotoUri(uri: Uri?) {
        _formState.value = _formState.value.copy(uri = uri)
    }


    fun onSaveDraftDismiss(navigateBack: () -> Unit) {
        _formState.value = _formState.value.copy(isSaveDraftModalOpen = false)
        navigateBack()

    }

    fun onSaveDraftConfirm(navigateBack: () -> Unit) {
        _formState.value = _formState.value.copy(isSaveDraftModalOpen = false)
        viewModelScope.launch {
            try {
                val savableImageUri = if (_formState.value.uri == null)
                    ""
                else
                    _formState.value.uri.toString()
                if(_formState.value.isFromDraft){
                    _formState.value.draftId?.let {
                        draftRepository.updateDraft(
                            postTitle = _formState.value.postTitle,
                            postBody = _formState.value.postBody,
                            imageUri = savableImageUri,
                            draftId = it,
                        )
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Your draft has been updated"))
                    }
                }else{
                    draftRepository.insertDraft(
                        DraftRecord(
                            postTitle = _formState.value.postTitle,
                            postBody = _formState.value.postBody,
                            imageUri = savableImageUri

                        )
                    )
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "Your draft has been saved"))
                }
                navigateBack()
            } catch (e: IOException) {
                _eventFlow.emit(UiEvent.ShowSnackbar(message = "Failed to save Draft"))
            }
        }
    }


    fun postArticle(navigateToDashboardScreen: () -> Unit, user: User?) {
        viewModelScope.launch {
            val postedOn = SimpleDateFormat("dd/MM/yyyy").format(Date())
            val postedAt = SimpleDateFormat("hh:mm:ss").format(Date())
            val username = user?.username ?: ""
            val postBody = PostBody(
                postTitle = _formState.value.postTitle,
                postBody = _formState.value.postBody,
                postedBy = username,
                postedOn = postedOn,
                postedAt = postedAt,
                photo = _formState.value.postTitle,
            )
            _formState.value.uri?.let {
                uploadPostWorkManager.startUpload(postBody = postBody, uri = it)
                navigateToDashboardScreen()
            }
        }
    }
}
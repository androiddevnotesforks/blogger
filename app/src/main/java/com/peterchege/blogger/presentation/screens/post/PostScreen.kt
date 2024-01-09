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
package com.peterchege.blogger.presentation.screens.post


import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Message
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.core.util.addThreeHoursToDateString
import com.peterchege.blogger.core.util.calculateDoubleTapOffset
import com.peterchege.blogger.core.util.calculateNewOffset
import com.peterchege.blogger.core.util.formatDateTime
import com.peterchege.blogger.domain.mappers.toPost
import com.peterchege.blogger.presentation.bottomsheets.CommentsBottomSheet
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.alertDialogs.DeleteCommentDialog
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PostScreen(
    viewModel: PostScreenViewModel = hiltViewModel()
) {
    val authUser by viewModel.authUserFlow.collectAsStateWithLifecycle(initialValue = null)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val commentUiState by viewModel.commentUiState.collectAsStateWithLifecycle()
    val deletePostUiState by viewModel.deletePostUiState.collectAsStateWithLifecycle()

    PostScreenContent(
        uiState = uiState,
        commentUiState = commentUiState,
        deletePostUiState = deletePostUiState,
        onLikePost = { it1 ->
            authUser?.let { user ->
                if (user.userId != "") {
                    viewModel.likePost(post = it1, user = user)
                }
            }
        },
        onUnlikePost = { it1 ->
            authUser?.let { user ->
                if (user.userId != "") {
                    viewModel.unLikePost(post = it1, user = user)
                }
            }
        },
        onFollowUser = viewModel::followUser,
        onUnFollowUser = viewModel::unfollowUser,
        onChangeNewComment = viewModel::onChangeComment,
        savePost = viewModel::savePostToRoom,
        unSavePost = viewModel::deletePostFromRoom,
        openCommentDialog = viewModel::onDialogOpen,
        closeCommentDialog = viewModel::onDialogDismiss,
        openDeleteDialog = viewModel::onDialogDeleteOpen,
        closeDeleteDialog = viewModel::onDialogDeleteDismiss,
        eventFlow = viewModel.eventFlow,
        user = authUser,
        postComment = {
            viewModel.onDialogConfirm(authUser!!)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun PostScreenContent(
    uiState: PostScreenUiState,
    commentUiState: CommentUiState,
    deletePostUiState: DeletePostUiState,
    onLikePost: (Post) -> Unit,
    onUnlikePost: (Post) -> Unit,
    onFollowUser: (User, String) -> Unit,
    onUnFollowUser: (User, String) -> Unit,
    onChangeNewComment: (String) -> Unit,
    savePost: (Post) -> Unit,
    unSavePost: (String) -> Unit,
    openCommentDialog: () -> Unit,
    closeCommentDialog: () -> Unit,
    openDeleteDialog: () -> Unit,
    closeDeleteDialog: () -> Unit,
    eventFlow: SharedFlow<UiEvent>,
    user: User?,
    postComment: () -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val commentsBottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.Navigate -> {

                }
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openCommentDialog()
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Message,
                    contentDescription = "Add a comment"
                )
            }
        }
    ) { paddingValues ->
        when (uiState) {
            is PostScreenUiState.Loading -> {
                LoadingComponent()
            }

            is PostScreenUiState.Error -> {
                ErrorComponent(
                    retryCallback = { /*TODO*/ },
                    errorMessage = "An unexpected error occurred"
                )
            }

            is PostScreenUiState.PostNotFound -> {
                ErrorComponent(
                    retryCallback = { /*TODO*/ },
                    errorMessage = "Post not found"
                )
            }

            is PostScreenUiState.Success -> {
                val post = uiState.post
                val comments = uiState.comments.collectAsLazyPagingItems()
                if (deletePostUiState.isDeleteDialogOpen) {
                    DeleteCommentDialog(
                        post = post.toPost(),
                        closeDeleteDialog = { closeDeleteDialog() },
                        deletePost = { },
                    )
                }
//                CommentDialog(
//                    closeCommentDialog = { closeCommentDialog() },
//                    commentUiState = commentUiState,
//                    postComment = { postComment() },
//                    onChangeNewComment = { onChangeNewComment(it) },
//                    isUserLoggedIn = uiState.isUserLoggedIn,
//                )
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        )
                        {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                var offset by remember { mutableStateOf(Offset.Zero) }
                                var zoom by remember { mutableStateOf(1f) }
                                SubcomposeAsyncImage(
                                    model = post.imageUrl,
                                    loading = {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.align(
                                                    Alignment.Center
                                                )
                                            )
                                        }
                                    },
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .height(300.dp)
                                        .clipToBounds()
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onDoubleTap = { tapOffset ->
                                                    zoom = if (zoom > 1f) 1f else 2f
                                                    offset = calculateDoubleTapOffset(
                                                        zoom = zoom,
                                                        size = size,
                                                        tapOffset = tapOffset
                                                    )

                                                }
                                            )
                                        }
                                        .pointerInput(Unit) {
                                            detectTransformGestures(
                                                onGesture = { centroid, pan, gestureZoom, _ ->
                                                    offset = offset.calculateNewOffset(
                                                        centroid = centroid,
                                                        pan = pan,
                                                        zoom = zoom,
                                                        gestureZoom = gestureZoom,
                                                        size = size
                                                    )
                                                    zoom = maxOf(a = 1f, b = zoom * gestureZoom)
                                                }
                                            )
                                        }
                                        .graphicsLayer {
                                            translationX = -offset.x * zoom
                                            translationY = -offset.y * zoom
                                            scaleX = zoom
                                            scaleY = zoom
                                            transformOrigin = TransformOrigin(
                                                pivotFractionX = 0f,
                                                pivotFractionY = 0f
                                            )
                                        },
                                    contentDescription = "Post Image"
                                )

                                Spacer(
                                    modifier = Modifier
                                        .height(8.dp)
                                        .padding(15.dp)
                                )
                                Divider(
                                    color = Color.Blue,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(10.dp)
                                )
                                Column(
                                    modifier = Modifier.padding(10.dp)

                                ) {
                                    Text(
                                        text = post.postTitle,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 30.sp
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp),
                                        Arrangement.SpaceEvenly
                                    ) {
                                        Text(text = "By: ")
                                        Text(
                                            text = post.postAuthor.fullName,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.clickable {
//                                                navController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/${state.post.postAuthor}")
                                            }
                                        )
                                        Icon(
                                            imageVector = if (post.isLiked)
                                                Icons.Default.Favorite
                                            else
                                                Icons.Default.FavoriteBorder,
                                            contentDescription = if (post.isLiked)
                                                "Un Like"
                                            else
                                                "Like",
                                            modifier = Modifier.clickable {
                                                if (user != null) {
                                                    if (post.isLiked) {
                                                        onUnlikePost(post.toPost())
                                                    } else {
                                                        onLikePost(post.toPost())
                                                    }

                                                }
                                            }
                                        )
                                        Icon(
                                            imageVector = if (post.isSaved)
                                                Icons.Default.Bookmark
                                            else
                                                Icons.Default.BookmarkBorder,
                                            contentDescription = if (post.isSaved) "Un Save" else "Save",
                                            modifier = Modifier.clickable {
                                                if (post.isSaved) {
                                                    unSavePost(post.postId)
                                                } else {
                                                    savePost(post.toPost())
                                                }
                                            }
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = "Share",
                                            modifier = Modifier.clickable {

                                            }
                                        )
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(8.dp)
                                            .padding(15.dp)
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp),
                                        Arrangement.SpaceEvenly,
                                        Alignment.CenterVertically

                                    ) {
                                        Text(text = formatDateTime(addThreeHoursToDateString(post.createdAt)))

                                        Text(text = "${post._count.likes} like(s)")
                                        Text(text = "${post._count.views} view(s)")

                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(8.dp)
                                            .padding(15.dp)
                                    )
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(10.dp),
                                    thickness = 1.dp,
                                    color = Color.Blue
                                )
                                Text(
                                    text = post.postBody,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier
                                        .padding(10.dp)

                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(8.dp)
                                        .padding(15.dp)
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp),
                                    text = "Comments (${post._count.comments})",
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 27.sp
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(10.dp),
                                    thickness = 1.dp,
                                    color = Color.Blue
                                )

                            }
                        }
                    }
                }
                if (commentUiState.isCommentsBottomSheetOpen) {
                    CommentsBottomSheet(
                        comments = comments,
                        commentsBottomSheetState = commentsBottomSheetState
                    )
                }
            }
        }
    }
}

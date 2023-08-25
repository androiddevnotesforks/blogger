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
package com.peterchege.blogger.presentation.screens.dashboard.feed_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.core.util.categories
import com.peterchege.blogger.domain.mappers.toPost
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.CategoryCard
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
@ExperimentalCoilApi
fun FeedScreen(
    navigateToPostScreen: (String) -> Unit,
    navigateToAuthUserProfileScreen: () -> Unit,
    navigateToAuthorProfileScreen: (String) -> Unit,
    navigateToSearchScreen: () -> Unit,
    navigateToAddPostScreen: () -> Unit,
    navigateToCategoryScreen: (String) -> Unit,
    viewModel: FeedScreenViewModel = hiltViewModel()
) {

    val authUser by viewModel.authUser.collectAsStateWithLifecycle()
    val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val feedScreenUiState by viewModel.feedScreenUiState.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isSyncing,
        onRefresh = { viewModel.refreshFeed() }
    )

    FeedScreenContent(
        navigateToPostScreen = navigateToPostScreen,
        navigateToAuthUserProfileScreen = navigateToAuthUserProfileScreen,
        navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,
        navigateToSearchScreen = navigateToSearchScreen,
        navigateToAddPostScreen = navigateToAddPostScreen,
        navigateToCategoryScreen = navigateToCategoryScreen,
        authUser = authUser,
        eventFlow = viewModel.eventFlow,
        networkStatus = networkStatus,
        pullRefreshState = pullRefreshState,
        uiState = feedScreenUiState,
        isRefreshing = isSyncing,
        retryCallback = { viewModel.refreshFeed() },
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalCoilApi
fun FeedScreenContent(
    navigateToPostScreen: (String) -> Unit,
    navigateToAuthUserProfileScreen: () -> Unit,
    navigateToAuthorProfileScreen: (String) -> Unit,
    navigateToAddPostScreen: () -> Unit,
    navigateToSearchScreen: () -> Unit,
    navigateToCategoryScreen: (String) -> Unit,
    isRefreshing: Boolean,
    authUser: User?,
    uiState: FeedScreenUiState,
    eventFlow: SharedFlow<UiEvent>,
    networkStatus: NetworkStatus,
    pullRefreshState: PullRefreshState,
    retryCallback: () -> Unit,

    ) {
    val snackbarHostState = SnackbarHostState()
    LaunchedEffect(key1 = networkStatus) {
        when (networkStatus) {
            is NetworkStatus.Unknown -> {}

            is NetworkStatus.Connected -> {}

            is NetworkStatus.Disconnected -> {
                snackbarHostState.showSnackbar(
                    message = "You are offline"
                )
            }
        }
    }

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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
        ,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        text = "Blogger "
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            navigateToSearchScreen()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Posts",
                            modifier = Modifier.size(26.dp)

                        )
                    }
                },
                scrollBehavior = scrollBehavior

            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToAddPostScreen()

                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Create Post"
                )

            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(paddingValues = it)

            ,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
//            PullRefreshIndicator(
//                refreshing = isRefreshing,
//                state = pullRefreshState,
//            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items(items = categories) { category ->
                    CategoryCard(
                        modifier = Modifier.height(30.dp),
                        navigateToCategoryScreen = navigateToCategoryScreen,
                        categoryItem = category
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                }
            }
            when (uiState) {
                is FeedScreenUiState.Empty -> {
                    ErrorComponent(
                        retryCallback = { retryCallback() },
                        errorMessage = "No posts were found"
                    )
                }

                is FeedScreenUiState.Loading -> {
                    LoadingComponent()
                }

                is FeedScreenUiState.Error -> {
                    ErrorComponent(
                        retryCallback = { retryCallback() },
                        errorMessage = uiState.message
                    )
                }

                is FeedScreenUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .pullRefresh(pullRefreshState)
                            .fillMaxSize()
                            .padding(defaultPadding)
                    ) {

                        items(items = uiState.posts) { post ->
                            ArticleCard(
                                post = post.toPost(),
                                onItemClick = {
                                    navigateToPostScreen(it._id)
                                },
                                onProfileNavigate = {
                                    navigateToAuthorProfileScreen(it)
                                },
                                onDeletePost = {},
                                isLiked = post.isLiked,
                                isSaved = post.isSaved,
                                isProfile = false,
                                profileImageUrl = "https://res.cloudinary.com/dhuqr5iyw/image/upload/v1640971757/mystory/profilepictures/default_y4mjwp.jpg"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                    }
                }
            }
        }

    }
}
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
package com.peterchege.blogger.presentation.screens.search

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.presentation.screens.search.tabs.SearchPostsTab
import com.peterchege.blogger.presentation.screens.search.tabs.SearchUsersTab
import com.peterchege.blogger.presentation.theme.MainWhiteColor
import kotlinx.coroutines.launch
import com.peterchege.blogger.R
import com.peterchege.blogger.core.fake.dummyPostList
import com.peterchege.blogger.domain.mappers.toPost
import com.peterchege.blogger.presentation.components.CustomIconButton
import com.peterchege.blogger.presentation.theme.BloggerTheme

@Composable
fun SearchScreen(
    navigateToPostScreen: (String) -> Unit,
    navigateToAuthorProfileScreen: (String) -> Unit,

    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()


    SearchScreenContent(
        uiState = uiState,
        searchQuery = searchQuery,
        onChangeSearchQuery = viewModel::onChangeSearchTerm,
        navigateToPostScreen = navigateToPostScreen,
        navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,
        networkStatus = networkStatus,
    )

}


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreenContent(

    uiState: SearchScreenUiState,
    searchQuery: String,
    onChangeSearchQuery: (String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
    navigateToAuthorProfileScreen: (String) -> Unit,
    networkStatus: NetworkStatus,
) {


    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(key1 = networkStatus) {
        when (networkStatus) {
            is NetworkStatus.Unknown -> {

            }

            is NetworkStatus.Disconnected -> {
                snackbarHostState.showSnackbar(message = "Not connected")
            }

            is NetworkStatus.Connected -> {}
        }

    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = CenterVertically
            ) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    query = searchQuery,
                    onQueryChange = onChangeSearchQuery,
                    placeholder = {
                        Text(text = "Search")
                    },
                    trailingIcon = {
                        CustomIconButton(
                            onClick = {},
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                        )
                    },
                    onSearch = {},
                    active = false,
                    onActiveChange = { },
                    content = {}
                )
            }
        },
    ) { paddingValues ->
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { 2 }
        )
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(10.dp),
        ) {
            Tabs(pagerState = pagerState)
            TabsContent(
                pagerState = pagerState,
                uiState = uiState,
                navigateToPostScreen = navigateToPostScreen,
                navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,
                onRetry = { onChangeSearchQuery(searchQuery) }
            )
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        stringResource(id = R.string.post_tab),
        stringResource(id = R.string.user_tab)
    )
    val scope = rememberCoroutineScope()

    TabRow(

        selectedTabIndex = pagerState.currentPage,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.background,
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 2.5.dp
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            val color by animateColorAsState(
                label = "Color Animation",
                targetValue = if (pagerState.currentPage == index)
                    MaterialTheme.colorScheme.onBackground
                else
                    MaterialTheme.colorScheme.primary
            )
            Tab(
                text = {
                    Text(
                        text = list[index],
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = color
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(
    pagerState: PagerState,
    uiState: SearchScreenUiState,
    navigateToAuthorProfileScreen: (String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
    onRetry: () -> Unit,
) {
    HorizontalPager(

        state = pagerState
    ) {
        AnimatedContent(
            targetState = pagerState,
            label = "Horizontal Pager",

            ) { pager ->
            when (pager.currentPage) {
                0 -> SearchPostsTab(
                    uiState = uiState,
                    navigateToPostScreen = navigateToPostScreen,
                    onRetry = onRetry
                )

                1 -> SearchUsersTab(
                    uiState = uiState,
                    navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,
                    onRetry = onRetry
                )

            }
        }

    }
}

@Preview
@Composable
fun SearchScreenContentPreviewLight() {
    BloggerTheme {
        SearchScreenContent(
            uiState = SearchScreenUiState.ResultsFound(
                posts = dummyPostList.map { it.toPost() },
                users = emptyList()
            ),
            searchQuery = "",
            onChangeSearchQuery = {},
            navigateToPostScreen = {},
            navigateToAuthorProfileScreen = {},
            networkStatus = NetworkStatus.Connected
        )
    }

}


@Preview
@Composable
fun SearchScreenContentPreviewDark() {
    BloggerTheme(darkTheme = true) {
        SearchScreenContent(
            uiState = SearchScreenUiState.ResultsFound(
                posts = dummyPostList.map { it.toPost() },
                users = emptyList()
            ),
            searchQuery = "",
            onChangeSearchQuery = {},
            navigateToPostScreen = {},
            navigateToAuthorProfileScreen = {},
            networkStatus = NetworkStatus.Connected
        )
    }

}
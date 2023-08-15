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
package com.peterchege.blogger.presentation.screens.search_screen

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.presentation.screens.search_screen.tabs.SearchPostsTab
import com.peterchege.blogger.presentation.screens.search_screen.tabs.SearchUsersTab
import com.peterchege.blogger.presentation.theme.MainWhiteColor
import com.peterchege.blogger.presentation.theme.defaultPadding
import com.peterchege.blogger.presentation.theme.testColor
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navigateToPostScreen: (String) -> Unit,
    navigateToAuthorProfileScreen: (String) -> Unit,

    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()


    SearchScreenContent(
        uiState = uiState,
        searchQuery = searchQuery,
        onChangeSearchQuery = viewModel::onChangeSearchTerm,
        navigateToPostScreen = navigateToPostScreen,
        navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,

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

    ) {

    val snackbarHostState = SnackbarHostState()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        onChangeSearchQuery(it)
                    },
                    placeholder = {
                        Text(
                            text = "Search",
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .clickable {
                        },
                    shape = RoundedCornerShape(size = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrect = true,
                        keyboardType = KeyboardType.Text,
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = MainWhiteColor,
                        focusedIndicatorColor = MainWhiteColor,
                        unfocusedIndicatorColor = MainWhiteColor,
                        disabledIndicatorColor = MainWhiteColor
                    ),
                    textStyle = TextStyle(color = Color.Black),
                    maxLines = 1,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Product",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {

                                }
                        )
                    }
                )
            }
        },
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { 2 }
        )
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(defaultPadding)
            ,
        ) {
            Tabs(pagerState = pagerState)
            TabsContent(
                pagerState = pagerState,
                uiState = uiState,
                navigateToPostScreen = navigateToPostScreen,
                navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,
            )
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(Constants.SEARCH_TYPE_POSTS, Constants.SEARCH_TYPE_USERS)
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = Color.White,
        contentColor = Color.White,
        divider = {
            TabRowDefaults.PrimaryIndicator(
//                thickness = 2.dp,
                color = Color.White
            )
        },
        indicator = { tabPositions ->
            SecondaryIndicator(
                height = 2.dp,
                color = testColor
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        list[index],
                        color = if (pagerState.currentPage == index) testColor else Color.LightGray
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
    navigateToPostScreen: (String) -> Unit
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> SearchPostsTab(
                uiState = uiState,
                navigateToPostScreen = navigateToPostScreen
            )

            1 -> SearchUsersTab(
                uiState = uiState,
                navigateToAuthorProfileScreen = navigateToAuthorProfileScreen
            )

        }
    }
}

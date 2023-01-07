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
package com.peterchege.blogger.ui.search_screen.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.peterchege.blogger.components.ArticleCard
import com.peterchege.blogger.ui.search_screen.SearchProductScreenViewModel
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchPostsTab(
    navHostController: NavHostController,

    viewModel: SearchProductScreenViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)

    ) {
        if (viewModel.isLoading.value){
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        }else{
            if (viewModel.searchPosts.value.isEmpty()){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "No posts found")
                }
            }else{
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                ) {
                    items(viewModel.searchPosts.value) { post ->
                        ArticleCard(
                            post = post,
                            onItemClick = {
                                navHostController.navigate(Screens.POST_SCREEN + "/${post._id}/${Constants.API_SOURCE}")
                            },
                            onProfileNavigate = {
                                viewModel.onProfileNavigate(it,navHostController,navHostController)
                            },
                            onDeletePost = {

                            },
                            isLiked = false,
                            isSaved = false,
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
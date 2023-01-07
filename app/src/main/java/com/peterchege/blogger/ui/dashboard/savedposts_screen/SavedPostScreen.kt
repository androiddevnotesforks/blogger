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
package com.peterchege.blogger.ui.dashboard.savedposts_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.peterchege.blogger.components.ArticleCard
import com.peterchege.blogger.room.entities.toPost
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SavedPostScreen(
    navHostController: NavHostController,
    bottomNavController: NavController,
    viewModel: SavedPostScreenViewModel = hiltViewModel()
){
    val posts = viewModel.posts.collectAsState(initial = emptyList())
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text= "Your saved posts",
                    )
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ){
            item{
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (posts.value.isEmpty()){
                        Text(
                            text = "You have no saved posts",
                            fontWeight = FontWeight.Bold,
                        )
                    }

                }
            }

            items(posts.value){ post ->
                ArticleCard(
                    post = post.toPost(),
                    onItemClick = {
                        navHostController.navigate(Screens.POST_SCREEN + "/${post.toPost()._id}/${Constants.ROOM_SOURCE}")
                    },
                    onProfileNavigate = {
                        viewModel.onProfileNavigate(it,bottomNavController,navHostController)
                    },
                    onDeletePost = {

                    },
                    profileImageUrl ="https://res.cloudinary.com/dhuqr5iyw/image/upload/v1640971757/mystory/profilepictures/default_y4mjwp.jpg",
                    isLiked = false,
                    isSaved =true,
                    isProfile = false
                )
            }
        }
    }


}
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
package com.peterchege.blogger.core.api


import com.peterchege.blogger.core.api.requests.*
import com.peterchege.blogger.core.api.responses.*
import okhttp3.RequestBody
import retrofit2.Response

import retrofit2.http.*


interface BloggerApi {

    @POST("/user/login")
    suspend fun loginUser(@Body user: LoginUser): Response<LoginResponse>

    @POST("/user/logout")
    suspend fun logoutUser(@Body user: LogoutUser):Response<LogoutResponse>

    @POST("/user/signup")
    suspend fun signUpUser(@Body user: SignUpUser):Response<SignUpResponse>

    @GET("/post/all")
    suspend fun getAllPosts():Response<AllPostsResponse>

    @POST("/post/upload")
    suspend fun uploadPost(@Body postBody: PostBody): Response<UploadPostResponse>

    @POST("/post/add")
    suspend fun postImage(
        @Body body: RequestBody
    ):Response<UploadPostResponse>

    @GET("/post/single/{postId}")
    suspend fun getPostById(@Path("postId") postId: String):Response<PostResponse>

    @DELETE("/post/delete/{postId}")
    suspend fun getDeletePostById(@Path("postId") postId: String):Response<DeleteResponse>

    @POST("/comment/add")
    suspend fun postComment(@Body commentbody: CommentBody):Response<CommentResponse>

    @POST("/like/add")
    suspend fun likePost(@Body likePost: LikePost):Response<LikeResponse>

    @POST("/like/remove")
    suspend fun unlikePost(@Body likePost: LikePost):Response<LikeResponse>

    @POST("/follower/follow")
    suspend fun followUser(@Body followUser: FollowUser):Response<FollowResponse>

    @POST("/follower/unfollow")
    suspend fun unfollowUser(@Body followUser: FollowUser):Response<FollowResponse>

    @GET("/user/profile/{username}")
    suspend fun getUserProfile(@Path("username") username: String): Response<ProfileResponse>

    @POST("/user/updateToken")
    suspend fun updateToken(@Body updateToken: UpdateToken):Response<UpdateTokenResponse>

    @GET("/post/search/{searchTerm}")
    suspend fun searchPost(@Path("searchTerm") searchTerm: String):Response<SearchPostResponse>

    @POST("/view/add")
    suspend fun addView(@Body viewer: Viewer):Response<ViewResponse>



}


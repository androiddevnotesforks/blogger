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
package com.peterchege.blogger.domain.repository

import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.requests.LikePost
import com.peterchege.blogger.core.api.requests.PostBody
import com.peterchege.blogger.core.api.requests.Viewer
import com.peterchege.blogger.core.api.responses.*
import com.peterchege.blogger.core.room.entities.PostRecord
import com.peterchege.blogger.core.room.entities.PostRecordWithCommentsLikesViews
import com.peterchege.blogger.core.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface PostRepository {

    fun getAllPosts():Flow<List<Post>>

    suspend fun uploadPost(body: RequestBody):NetworkResult<UploadPostResponse>

    suspend fun getPostById(postId: String):NetworkResult<PostResponse>

    suspend fun deletePostFromApi(postId: String):NetworkResult<DeleteResponse>

    suspend fun addView(viewer: Viewer):NetworkResult<ViewResponse>

    suspend fun likePost(likePost: LikePost):NetworkResult<LikeResponse>

    suspend fun unlikePost(likePost: LikePost):NetworkResult<LikeResponse>

    suspend fun followUser(followUser: FollowUser):NetworkResult<FollowResponse>

    suspend fun unfollowUser(followUser: FollowUser):NetworkResult<FollowResponse>

    suspend fun searchPosts(searchTerm:String):NetworkResult<SearchPostResponse>


    suspend fun insertSavedPost(post: Post)

    suspend fun deleteAllSavedPosts()

    suspend fun deleteSavedPostById(id: String)

    suspend fun getSavedPost(postId: String): PostRecordWithCommentsLikesViews?


    fun getAllSavedPosts(): Flow<List<PostRecordWithCommentsLikesViews>>
}
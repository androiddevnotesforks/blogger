/*
 * Copyright 2024 Blogger
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
package com.peterchege.blogger.data.local.posts.likes

import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.room.database.BloggerDatabase
import com.peterchege.blogger.core.room.entities.LikeEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LikesLocalDataSourceImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val db:BloggerDatabase,
) :LikesLocalDataSource{

    override suspend fun deleteAllLikes() {
        withContext(ioDispatcher){
            db.likeDao.deleteAllLikes()
        }
    }

    override fun getAllLikes(): Flow<List<LikeEntity>> {
        return db.likeDao.getAllLikes().flowOn(ioDispatcher)
    }

    override suspend fun insertLike(like: LikeEntity) {
        withContext(ioDispatcher){
            db.likeDao.insertLike(like)
        }
    }

    override suspend fun deleteLike(postId: String) {
        withContext(ioDispatcher){
            db.likeDao.deleteLike(postId = postId)
        }
    }
    override suspend fun insertLikes(likes: List<LikeEntity>) {
        withContext(ioDispatcher){
            db.likeDao.insertLikes(likes)
        }
    }
}
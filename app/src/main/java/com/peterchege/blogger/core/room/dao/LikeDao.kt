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
package com.peterchege.blogger.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peterchege.blogger.core.room.entities.LikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikes(likes: List<LikeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like:LikeEntity)

    @Query("SELECT * FROM likes")
    fun getAllLikes(): Flow<List<LikeEntity>>

    @Query("DELETE FROM likes WHERE likepostId =:postId")
    suspend fun deleteLike(postId:String)

    @Query("DELETE FROM likes")
    suspend fun deleteAllLikes()

}
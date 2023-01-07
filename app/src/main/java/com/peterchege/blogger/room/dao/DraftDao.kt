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
package com.peterchege.blogger.room.dao

import androidx.room.*
import com.peterchege.blogger.room.entities.DraftRecord


@Dao
interface DraftDao {
    @Query("SELECT * FROM draft")
    suspend fun getAllDrafts():List<DraftRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: DraftRecord)

    @Query("SELECT * FROM draft where id = :id")
    suspend fun getDraftById(id:Int):DraftRecord

    @Query("DELETE FROM draft where id = :id")
    suspend fun deleteDraftById(id:Int)

    @Query("DELETE FROM draft")
    suspend fun deleteAllDrafts()






}
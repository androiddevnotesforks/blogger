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
package com.peterchege.blogger.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peterchege.blogger.api.responses.Post


@Entity(tableName = "post")
data class PostRecord(
    @PrimaryKey
    val _id: String,
    val postTitle: String,
    val postBody: String,
    val ImageUrl: String,
    val postedAt: String,
    val postAuthor: String,
    val postedOn: String,
)

fun PostRecord.toPost():Post{
    return Post(
        _id,
        postTitle,
        postBody,
        postAuthor,
        imageUrl = ImageUrl,
        postedAt,
        postedOn,
        emptyList(),
        emptyList(),
        emptyList(),

    )

}
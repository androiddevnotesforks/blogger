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
package com.peterchege.blogger.ui.signup

import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.api.requests.SignUpUser
import com.peterchege.blogger.api.responses.SignUpResponse
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val api: BloggerApi
) {
    suspend fun signUpUser(signUpUser: SignUpUser) : SignUpResponse {
        return api.signUpUser(signUpUser)
    }


}
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
package com.peterchege.blogger.core.analytics.analytics

import com.peterchege.blogger.core.analytics.analytics.AnalyticsEvent.Param

fun AnalyticsHelper.logLoginEvent(username: String) {
    logEvent(
        AnalyticsEvent(
            type = "log_in",
            extras = listOf(
                Param(key = "username", value = username),
            ),
        ),
    )
}

fun AnalyticsHelper.logSignUpEvent(email: String) {
    logEvent(
        AnalyticsEvent(
            type = "sign_up",
            extras = listOf(
                Param(key = "email", value = email),
            ),
        ),
    )
}

fun AnalyticsHelper.logLogOutEvent(username: String) {
    logEvent(
        AnalyticsEvent(
            type = "log_out",
            extras = listOf(
                Param(key = "username", value = username),
            ),
        ),
    )
}

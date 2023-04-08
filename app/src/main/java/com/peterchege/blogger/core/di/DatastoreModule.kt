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
package com.peterchege.blogger.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.peterchege.blogger.core.datastore.repository.UserDataStoreRepository
import com.peterchege.blogger.core.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatastoreModule {

    @Provides
    @Singleton
    fun provideUserDatastoreRepository(@ApplicationContext context: Context):
            UserDataStoreRepository{
        return UserDataStoreRepository(context = context)
    }


    @Provides
    @Singleton
    fun provideDatastorePreferences(@ApplicationContext context: Context):
            DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(Constants.USER_PREFERENCES)
            }
        )
//    @Provides
//    @Singleton
//    fun provideUserSettingPreferences(dataStore: DataStore<Preferences>): UserSettingsPreferences {
//        return UserSettingsPreferences(dataStore = dataStore)
//    }

}
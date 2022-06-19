package com.deva.submission_intermediate.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.deva.submission_intermediate.api.ApiConfig
import com.deva.submission_intermediate.data.StoryRepository
import com.deva.submission_intermediate.database.StoryDatabase
import com.deva.submission_intermediate.model.UserPreference


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService, userPreference)
    }
}
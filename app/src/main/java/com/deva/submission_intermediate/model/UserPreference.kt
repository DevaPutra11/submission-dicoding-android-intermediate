package com.deva.submission_intermediate.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    data class User(val name:String,val token:String)

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }

    }

    fun getToken():Flow<String>{
        return  dataStore.data.map { preferences->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    fun getInfoUser():Flow<User>{
        return  dataStore.data.map { preferences ->
            User(
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: ""
            )
        }
    }

    fun isLogin():Flow<Boolean>{
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY] ?: false
        }
    }

    suspend fun saveInfoUser(token:String,name:String){
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[NAME_KEY] = name
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences[TOKEN_KEY] = ""
        }
    }
}
package com.deva.submission_intermediate.view.main

import androidx.lifecycle.*
import com.deva.submission_intermediate.model.UserPreference
import kotlinx.coroutines.launch
class MainViewModel(private val pref: UserPreference) : ViewModel() {
    fun isLogin():LiveData<Boolean>{
        return pref.isLogin().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}
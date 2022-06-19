package com.deva.submission_intermediate.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.deva.submission_intermediate.model.UserPreference

class SplashViewModel(private val pref: UserPreference) : ViewModel() {
    fun isLogin(): LiveData<Boolean> {
        return pref.isLogin().asLiveData()
    }
}
package com.deva.submission_intermediate.view.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.deva.submission_intermediate.model.UserPreference

class CameraViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUserInfo(): LiveData<UserPreference.User> {
        return pref.getInfoUser().asLiveData()
    }
}
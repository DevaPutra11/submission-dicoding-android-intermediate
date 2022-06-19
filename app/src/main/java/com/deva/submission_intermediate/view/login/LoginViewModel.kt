package com.deva.submission_intermediate.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deva.submission_intermediate.model.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref:UserPreference) : ViewModel() {
    fun saveToken(token:String,name:String){
        viewModelScope.launch {
            pref.saveInfoUser(token,name)
        }
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }
}
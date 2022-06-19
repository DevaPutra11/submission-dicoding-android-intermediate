package com.deva.submission_intermediate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.deva.submission_intermediate.di.Injection
import com.deva.submission_intermediate.view.map.MapsViewModel
import com.deva.submission_intermediate.model.UserPreference
import com.deva.submission_intermediate.view.camera.CameraViewModel
import com.deva.submission_intermediate.view.login.LoginViewModel
import com.deva.submission_intermediate.view.main.MainViewModel
import com.deva.submission_intermediate.view.main.MainViewModel2
import com.deva.submission_intermediate.view.splash.SplashViewModel

class ViewModelFactory(private val pref: UserPreference, private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MainViewModel2::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel2(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MapsViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(CameraViewModel::class.java) -> {
                CameraViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
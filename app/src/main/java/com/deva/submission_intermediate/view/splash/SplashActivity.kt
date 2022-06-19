package com.deva.submission_intermediate.view.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.deva.submission_intermediate.R
import com.deva.submission_intermediate.ViewModelFactory
import com.deva.submission_intermediate.model.UserPreference
import com.deva.submission_intermediate.view.login.LoginActivity
import com.deva.submission_intermediate.view.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            setupViewModel()
            finish()
        }, TIME)
    }

    private fun setupViewModel() {
        splashViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[SplashViewModel::class.java]

        splashViewModel.isLogin().observe(this) {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    companion object{
        private const val TIME:Long = 3000
    }
}
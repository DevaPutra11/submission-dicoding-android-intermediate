package com.deva.submission_intermediate.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.deva.submission_intermediate.R
import com.deva.submission_intermediate.ViewModelFactory
import com.deva.submission_intermediate.api.ApiConfig
import com.deva.submission_intermediate.databinding.ActivityLoginBinding
import com.deva.submission_intermediate.model.UserPreference
import com.deva.submission_intermediate.response.LoginResponse
import com.deva.submission_intermediate.view.main.MainActivity
import com.deva.submission_intermediate.view.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupAction()
        playAnimation()

        binding.tvRegisterHere.setOnClickListener {
            val i = Intent(this,RegisterActivity::class.java)
            startActivity(i)
        }
    }

    private fun setupViewModel() {
        showLoading(false)
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[LoginViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.customEmailEditText.text.toString()
            val password = binding.customPasswordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.customEmailEditText.error = getString(R.string.enter_email)
                }
                password.isEmpty() -> {
                    binding.customPasswordEditText.error = getString(R.string.enter_password)
                }
                password.length < 6 -> {
                    binding.customPasswordEditText.error = getString(R.string.valid_password)
                }
                !isEmailValid(email) ->{
                    binding.customEmailEditText.error = getString(R.string.valid_email)
                }
                else -> {
                    showLoading(true)
                    val client = ApiConfig.getApiService().login(email, password)
                    client.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            if (response.isSuccessful) {
                                showLoading(false)
                                val responseBody = response.body()
                                if (responseBody != null && !responseBody.error) {
                                    Log.d("Loginnn logd",responseBody.message)
                                    loginViewModel.login()
                                    loginViewModel.saveToken(
                                        responseBody.loginResult.token,
                                        responseBody.loginResult.name
                                    )

                                    token = responseBody.loginResult.token

                                    AlertDialog.Builder(this@LoginActivity).apply {
                                        setTitle(getString(R.string.succes))
                                        setMessage(getString(R.string.succes_login))
                                        setPositiveButton(getString(R.string.cont)) { _, _ ->
                                            val intent = Intent(context, MainActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(intent)
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
                                }
                            } else {
                                showLoading(false)
                                Log.d("Loginnnn logd", response.message())
                                Log.e("Login", "onFailure: ${response.message()}")
                                AlertDialog.Builder(this@LoginActivity).apply {
                                    setTitle(getString(R.string.fail))
                                    setMessage(getString(R.string.fail_login))
                                    setPositiveButton(getString(R.string.back)) { _, _ ->
                                        show().dismiss()
                                    }
                                    create()
                                    show()
                                }
                            }
                        }
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            showLoading(false)
                            Log.e("Login", "onFailure retrofit: ${t.message}")
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle(getString(R.string.fail))
                                setMessage(getString(R.string.fail_internet))
                                setPositiveButton(getString(R.string.back)) { _, _ ->
                                    show().dismiss()
                                }
                                create()
                                show()
                            }
                        }
                    })
                }
            }
        }
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {
        val imgLogin = ObjectAnimator.ofFloat(binding.imgLogin, View.ALPHA,1f).setDuration(500)

        val login = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA,1f).setDuration(500)

        val email = ObjectAnimator.ofFloat(binding.tvEmailLogin, View.ALPHA,1f).setDuration(500)
        val emailLayout = ObjectAnimator.ofFloat(binding.customEmailEditText, View.ALPHA,1f).setDuration(500)

        val password = ObjectAnimator.ofFloat(binding.tvPasswordLogin, View.ALPHA,1f).setDuration(500)
        val passwordLayout = ObjectAnimator.ofFloat(binding.customPasswordEditText, View.ALPHA,1f).setDuration(500)

        val buttonLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA,1f).setDuration(500)

        val regHere = ObjectAnimator.ofFloat(binding.regHere, View.ALPHA,1f).setDuration(500)

        val animEmail = AnimatorSet().apply {
            playTogether(email,emailLayout)
        }

        val animPassword = AnimatorSet().apply {
            playTogether(password,passwordLayout)
        }

        AnimatorSet().apply {
            playSequentially(imgLogin,login,animEmail,animPassword,buttonLogin,regHere)
            start()
        }
    }

    private fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email.toString())
        return matcher.matches()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object{
        var token:String = "token"
    }
}
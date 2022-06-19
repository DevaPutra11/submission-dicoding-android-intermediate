package com.deva.submission_intermediate.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.deva.submission_intermediate.R
import com.deva.submission_intermediate.api.ApiConfig
import com.deva.submission_intermediate.databinding.ActivityRegisterBinding
import com.deva.submission_intermediate.response.RegisterResponse
import com.deva.submission_intermediate.view.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        showLoading(false)
        setupAction()
        playAnimation()

        binding.tvLoginhere.setOnClickListener {
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
        }
    }

    private fun setupAction() {
        binding.btnSignup.setOnClickListener {
            showLoading(true)
            val name = binding.customNameEditText.text.toString()
            val email = binding.customEmailEditText.text.toString()
            val password = binding.customPasswordEditText.text.toString()

            when {
                name.isEmpty() -> {
                    showLoading(false)
                    binding.customNameEditText.error = getString(R.string.enter_name)
                }
                email.isEmpty() -> {
                    showLoading(false)
                    binding.customEmailEditText.error = getString(R.string.enter_email)
                }
                password.isEmpty() -> {
                    showLoading(false)
                    binding.customPasswordEditText.error = getString(R.string.enter_password)
                }
                password.length < 6 -> {
                    showLoading(false)
                    binding.customPasswordEditText.error = getString(R.string.valid_password)
                }
                !isEmailValid(email) ->{
                    showLoading(false)
                    binding.customEmailEditText.error = getString(R.string.valid_email)
                }
                else -> {
                    val client = ApiConfig.getApiService().register(name,email, password)
                    client.enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(
                            call: Call<RegisterResponse>,
                            response: Response<RegisterResponse>
                        ) {
                            if (response.isSuccessful) {
                                showLoading(false)
                                val responseBody = response.body()
                                if (responseBody != null && !responseBody.error) {
                                    Log.d("Registerrr logd",responseBody.message)
                                    AlertDialog.Builder(this@RegisterActivity).apply {
                                        setTitle(getString(R.string.succes))
                                        setMessage(getString(R.string.succes_register))
                                        setPositiveButton(getString(R.string.cont)) { _, _ ->
                                            val intent = Intent(context, LoginActivity::class.java)
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
                                Log.d("Registerrr logd", response.message())
                                Log.e("Register", "onFailure: ${response.message()}")

                                AlertDialog.Builder(this@RegisterActivity).apply {
                                    setTitle(getString(R.string.fail))
                                    setMessage(getString(R.string.fail_register))
                                    setPositiveButton(getString(R.string.back)) { _, _ ->
                                        show().dismiss()
                                    }
                                    create()
                                    show()
                                }
                            }
                        }
                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            showLoading(false)
                            Log.e("Register", "onFailure retrofit: ${t.message}")
                            AlertDialog.Builder(this@RegisterActivity).apply {
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
        val imgRegister = ObjectAnimator.ofFloat(binding.imgRegister, View.ALPHA,1f).setDuration(500)

        val signUp = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA,1f).setDuration(500)

        val nama = ObjectAnimator.ofFloat(binding.tvNamaRegister, View.ALPHA,1f).setDuration(500)
        val namaLayout = ObjectAnimator.ofFloat(binding.customNameEditText, View.ALPHA,1f).setDuration(500)

        val email = ObjectAnimator.ofFloat(binding.tvEmailRegister, View.ALPHA,1f).setDuration(500)
        val emailLayout = ObjectAnimator.ofFloat(binding.customEmailEditText, View.ALPHA,1f).setDuration(500)

        val password = ObjectAnimator.ofFloat(binding.tvPasswordRegister, View.ALPHA,1f).setDuration(500)
        val passwordLayout = ObjectAnimator.ofFloat(binding.customPasswordEditText, View.ALPHA,1f).setDuration(500)

        val buttonSignUp = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA,1f).setDuration(500)

        val loginHere = ObjectAnimator.ofFloat(binding.loginHere, View.ALPHA,1f).setDuration(500)

        val animName = AnimatorSet().apply {
            playTogether(nama,namaLayout)
        }

        val animEmail = AnimatorSet().apply {
            playTogether(email,emailLayout)
        }

        val animPassword = AnimatorSet().apply {
            playTogether(password,passwordLayout)
        }

        AnimatorSet().apply {
            playSequentially(imgRegister,signUp, animName, animEmail,animPassword,buttonSignUp,loginHere)
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
}
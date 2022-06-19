package com.deva.submission_intermediate.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.deva.submission_intermediate.R
import com.deva.submission_intermediate.databinding.ActivityDetailBinding
import com.deva.submission_intermediate.model.UserModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<UserModel>(EXTRA_USER)
        setDetail(user?.name ?: "null",
            user?.photoUrl ?: "null",
            user?.description ?: "null"
        )

        supportActionBar?.title = getString(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


    private fun setDetail(name:String,urlImage:String,desc:String){
        binding.tvNameDetail.text = name
        binding.tvDescriptionDetail.text = desc
        Glide.with(this)
            .load(urlImage)
            .into(binding.imgStoryDetail)
    }

    companion object{
        const val EXTRA_USER = "extra_user"
    }
}
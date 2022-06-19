package com.deva.submission_intermediate.view.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.deva.submission_intermediate.R
import com.deva.submission_intermediate.ViewModelFactory
import com.deva.submission_intermediate.adapter.ListStoriesAdapter
import com.deva.submission_intermediate.adapter.LoadingStateAdapter
import com.deva.submission_intermediate.databinding.ActivityMainBinding
import com.deva.submission_intermediate.model.UserPreference
import com.deva.submission_intermediate.view.camera.AddStoryActivity
import com.deva.submission_intermediate.view.login.LoginActivity
import com.deva.submission_intermediate.view.map.MapsActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private val mainViewModel2: MainViewModel2 by viewModels {
        ViewModelFactory(UserPreference.getInstance(dataStore),this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.app_name)
        binding.rvStories.layoutManager = LinearLayoutManager(this)

        showLoading(true)
        setupViewModel()

        getData()

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun getData() {
        showLoading(false)

        val adapter = ListStoriesAdapter()
        binding.rvStories.adapter = adapter
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel2.story.observe(this) {
            adapter.submitData(lifecycle,it)
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[MainViewModel::class.java]

        mainViewModel.isLogin().observe(this) {
            if (!it) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_select_languange -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.menu_log_out -> {
                mainViewModel.logout()
                true
            }
            R.id.menu_map -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}
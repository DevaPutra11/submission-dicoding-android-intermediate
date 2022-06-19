package com.deva.submission_intermediate.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deva.submission_intermediate.data.StoryRepository
import com.deva.submission_intermediate.response.StoriesResponse

class MapsViewModel(private val storyRepository: StoryRepository): ViewModel() {
    private val _stories = MutableLiveData<StoriesResponse>()
    val stories: LiveData<StoriesResponse> = _stories

    fun getStories(){
        _stories.value = storyRepository.getStories()
    }
}
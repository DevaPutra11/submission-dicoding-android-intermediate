package com.deva.submission_intermediate.view.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.deva.submission_intermediate.data.StoryRepository
import com.deva.submission_intermediate.response.ListStoryItem

class MainViewModel2(private val storyRepository: StoryRepository) : ViewModel() {
    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}
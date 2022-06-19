package com.deva.submission_intermediate.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.deva.submission_intermediate.api.ApiService
import com.deva.submission_intermediate.database.StoryDatabase
import com.deva.submission_intermediate.model.UserPreference
import com.deva.submission_intermediate.response.ListStoryItem
import com.deva.submission_intermediate.response.StoriesResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val pref: UserPreference) {

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, getToken()),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getStories():StoriesResponse{
        return runBlocking {
            apiService.getPagingStories(
                "Bearer ${getToken()}",
                1,
                10,
                1
            )
        }
    }

    private fun getToken():String{
        return runBlocking{
            pref.getToken().first()
        }
    }
}
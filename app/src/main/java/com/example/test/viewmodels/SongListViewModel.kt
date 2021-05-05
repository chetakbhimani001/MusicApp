package com.example.test.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.models.FeedEntry
import com.example.test.repositories.networkRepository.SongsRepository

class SongListViewModel() : ViewModel() {

    private var listData = MutableLiveData<ArrayList<FeedEntry>>()

    init {
        val songRepository: SongsRepository by lazy {
            SongsRepository
        }

        listData = songRepository.getMutableLiveData()

    }

    fun getData(): MutableLiveData<ArrayList<FeedEntry>> {
        return listData
    }

}
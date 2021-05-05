package com.example.test.repositories.networkRepository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.data.room.entity.SongEntity
import com.example.test.api.ApiClient
import com.example.test.models.FeedEntry
import com.example.test.models.FeedResponse
import com.example.test.utils.Utility.Companion.hideProgressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object SongsRepository {

    fun getMutableLiveData() : MutableLiveData<ArrayList<FeedEntry>>{

        val mutableLiveData = MutableLiveData<ArrayList<FeedEntry>>()

        ApiClient.apiService.getTopSongs(25).enqueue(object : Callback<FeedResponse> {
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                hideProgressBar()
                Log.e("error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<FeedResponse>,
                response: Response<FeedResponse>
            ) {
                hideProgressBar()
                val songsResponse = response.body()
                songsResponse?.let { mutableLiveData.value = it.feed as ArrayList<FeedEntry> }
            }

        })

        return mutableLiveData
    }

}
package com.example.test.api

import com.example.test.models.FeedResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

@POST("topsongs/limit={limit}/xml")
    fun getTopSongs(@Path("limit") limit: Int): Call<FeedResponse>

}


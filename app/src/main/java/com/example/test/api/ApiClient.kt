package com.example.test.api

import android.provider.SyncStateContract
import com.example.test.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {

    private const val BASE_URL: String = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/"

    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }


    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getHttpClient())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    val apiService : ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }
    private fun getHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val httpClint = OkHttpClient.Builder()

        httpClint.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
            request.addHeader("Accept", "application/json")
            val requestBuilder = request.build()
            chain.proceed(requestBuilder)
        }
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
        httpClint.addInterceptor(logging)

        return httpClint.build()
    }
}
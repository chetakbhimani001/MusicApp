package com.example.test.repositories.networkRepository


sealed class ApiResponse<out T : Any> {

    data class Success<out T : Any>(val data: T) : ApiResponse<T>()

    data class Error(val error: String, val status: Int = 0) : ApiResponse<Nothing>()
}

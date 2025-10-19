package com.hadeer.jetpackcomposepokemon.util

import java.io.IOException

sealed class Resource<T>(val data: T? = null, val message : String? = null){

    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>() : Resource<T>()
}


sealed interface NetworkResponse <out T:Any>{
    data class Success<T : Any>(val body: T) : NetworkResponse<T>

    /**
     * Failure response with body
     */
    data class ApiError(val body: String, val code: Int) : NetworkResponse<Nothing>

    /**
     * Network error
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing>

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable) : NetworkResponse<Nothing>
}
package com.alpha.trafficconditionmap.network

import java.io.IOException

sealed class ApiResponse<out T : Any, out U : Any> {

    //For api call success with body
    data class Success<T : Any>(val body: T) : ApiResponse<T, Nothing>()

    //For api call failure with body
    data class ApiError<U : Any>(val body: U, val code: Int) : ApiResponse<Nothing, U>()

    //For connectivity error
    data class NetworkError(val error: IOException) : ApiResponse<Nothing, Nothing>()

    //For unknown errors eg: parsing error
    data class UnknownError(val error: Throwable?) : ApiResponse<Nothing, Nothing>()
}

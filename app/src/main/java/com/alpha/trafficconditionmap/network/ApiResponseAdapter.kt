package com.alpha.trafficconditionmap.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

class ApiResponseAdapter<S : Any, E : Any>(
    private val successType: Type,
    private val errorConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<ApiResponse<S, E>>> {

    override fun adapt(call: Call<S>): Call<ApiResponse<S, E>> =
        ApiResponseCall(call, errorConverter)

    override fun responseType() = successType
}

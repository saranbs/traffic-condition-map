package com.alpha.trafficconditionmap.network

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

class ApiResponseCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<ApiResponse<S, E>> {

    override fun enqueue(callback: Callback<ApiResponse<S, E>>) =
        delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val responseBody = response.body()
                val code: Int = response.code()
                val errorBody = response.errorBody()

                if (response.isSuccessful) {
                    if (responseBody != null) {
                        callback.onResponse(
                            this@ApiResponseCall,
                            Response.success(ApiResponse.Success(responseBody))
                        )
                    } else {
                        callback.onResponse(
                            this@ApiResponseCall,
                            Response.success(ApiResponse.UnknownError(null))
                        )
                    }
                } else {

                    val body = when {
                        errorBody == null -> null
                        errorBody.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(errorBody)
                        } catch (ex: Exception) {
                            null
                        }
                    }

                    return if (body != null) {
                        callback.onResponse(
                            this@ApiResponseCall,
                            Response.success(ApiResponse.ApiError(body, code))
                        )
                    } else {
                        callback.onResponse(
                            this@ApiResponseCall,
                            Response.success(ApiResponse.UnknownError(null))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                if (throwable is IOException) {
                    callback.onResponse(
                        this@ApiResponseCall,
                        Response.success(ApiResponse.NetworkError(throwable))
                    )
                } else {
                    callback.onResponse(
                        this@ApiResponseCall,
                        Response.success(ApiResponse.UnknownError(throwable))
                    )
                }
            }

        })

    override fun isExecuted() = delegate.isExecuted

    override fun timeout(): Timeout = delegate.timeout()

    override fun clone() = ApiResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<ApiResponse<S, E>> {
        throw UnsupportedOperationException("execute not supported")
    }

    override fun request(): Request = delegate.request()
}

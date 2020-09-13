package com.alpha.trafficconditionmap.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        check(returnType is ParameterizedType) {
            "return type must be parameterized"
        }

        val type = getParameterUpperBound(0, returnType)
        if (getRawType(type) != ApiResponse::class.java) {
            return null
        }

        check(type is ParameterizedType) {
            "type of response must be parameterized"
        }

        val successType = getParameterUpperBound(0, type)
        val errorType = getParameterUpperBound(1, type)

        val errorConverter = retrofit.nextResponseBodyConverter<Any>(null, errorType, annotations)

        return ApiResponseAdapter<Any, Any>(successType, errorConverter)
    }
}

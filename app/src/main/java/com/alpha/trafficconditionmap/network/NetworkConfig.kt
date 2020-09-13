package com.alpha.trafficconditionmap.network

interface NetworkConfig {

    fun baseUrl(): String

    fun isDebug(): Boolean

    fun connectTimeoutInSeconds(): Long

    fun readTimeoutInSeconds(): Long

    fun writeTimeoutInSeconds(): Long
}

package com.alpha.trafficconditionmap

import com.alpha.trafficconditionmap.network.NetworkConfig

class TrafficConditionMapNetworkConfig : NetworkConfig {
    override fun baseUrl() = "https://api.data.gov.sg/v1/"

    override fun isDebug() = true

    override fun connectTimeoutInSeconds() = 120L

    override fun readTimeoutInSeconds() = 120L

    override fun writeTimeoutInSeconds() = 120L
}

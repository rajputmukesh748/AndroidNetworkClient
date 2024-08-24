package com.androidnetworkclient.network

import com.networkclient.NetworkClient
import com.networkclient.enums.LoggerLevel
import io.ktor.client.HttpClient

object ApiClient {

    fun getClient(): HttpClient = NetworkClient.Builder()
        .addBaseUrl("https://dummyjson.com/")
        .connectTimeout(30000)
        .requestTimeout(30000)
        .socketTimeout(30000)
        .enableRetryCount(isRetry = true, retryCount = 1)
        .addLoggerInterceptor(LoggerLevel.ALL){  }
        .build()

}
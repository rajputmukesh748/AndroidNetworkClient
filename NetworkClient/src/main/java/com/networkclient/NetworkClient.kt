package com.networkclient

import com.networkclient.client.CustomHttpClient
import com.networkclient.enums.LoggerLevel
import com.networkclient.interfaces.KtorInterceptor
import com.networkclient.interfaces.LoggerInterface
import com.networkclient.models.LoggerData
import com.networkclient.models.NetworkModel
import com.networkclient.utils.getLoggerLevel
import io.ktor.client.HttpClient

/**
 * A singleton object for configuring and creating an [HttpClient] instance.
 *
 * This object provides a builder pattern for constructing a customized `HttpClient` instance
 * with various configurations such as base URL, logging settings, and timeout values.
 */
object NetworkClient {

    // Default NetworkModel instance, initialized lazily
    private val networkModel by lazy { NetworkModel(baseUrl = "") }

    /**
     * Builder class for constructing a customized [HttpClient] instance.
     *
     * This class allows you to set various configurations for the network client, including base URL,
     * logger settings, and timeout values. It provides methods to modify these settings and then
     * build the final `HttpClient` instance.
     */
    class Builder {

        /**
         * Sets the base URL for the network client.
         *
         * @param url The base URL to be used for all network requests.
         * @return The current [Builder] instance for method chaining.
         */
        fun addBaseUrl(url: String): Builder {
            networkModel.baseUrl = url
            return this
        }

        /**
         * Configures the logger for the network client.
         *
         * @param level The logging level to be used.
         * @param callback The optional [LoggerInterface] for custom logging behavior.
         * @return The current [Builder] instance for method chaining.
         */
        fun addLoggerInterceptor(level: LoggerLevel, callback: LoggerInterface?): Builder {
            networkModel.logger = LoggerData(
                logLevel = level.getLoggerLevel(),
                loggerInterface = callback
            )
            return this
        }

        /**
         * Adds a [KtorInterceptor] to the list of network interceptors.
         *
         * This method allows you to add a custom interceptor that will be used to intercept and
         * modify HTTP requests and responses. Interceptors can be used for tasks such as logging,
         * authentication, or modifying request/response headers.
         *
         * @param interceptor The [KtorInterceptor] instance to be added to the list. This interceptor
         *                    will be applied to all network requests and responses handled by the
         *                    network configuration.
         * @return The current [Builder] instance, allowing for method chaining.
         */
        fun addNetworkInterceptor(interceptor: KtorInterceptor): Builder {
            networkModel.interceptors.add(interceptor)
            return this
        }

        /**
         * Sets the request timeout for the network client.
         *
         * @param millisecond The timeout duration in milliseconds for the request.
         * @return The current [Builder] instance for method chaining.
         */
        fun requestTimeout(millisecond: Long): Builder {
            networkModel.networkPoolingTimeout.requestTimeout = millisecond
            return this
        }

        /**
         * Sets the connection timeout for the network client.
         *
         * @param millisecond The timeout duration in milliseconds for establishing a connection.
         * @return The current [Builder] instance for method chaining.
         */
        fun connectTimeout(millisecond: Long): Builder {
            networkModel.networkPoolingTimeout.connectTimeout = millisecond
            return this
        }

        /**
         * Sets the socket timeout for the network client.
         *
         * @param millisecond The timeout duration in milliseconds for waiting for data on the socket.
         * @return The current [Builder] instance for method chaining.
         */
        fun socketTimeout(millisecond: Long): Builder {
            networkModel.networkPoolingTimeout.socketTimeout = millisecond
            return this
        }

        fun enableRetryCount(isRetry: Boolean, retryCount: Int): Builder {
            networkModel.retryEnable = isRetry
            networkModel.retryCount = retryCount
            return this
        }

        /**
         * Builds and returns a customized [HttpClient] instance based on the configured settings.
         *
         * @return The configured [HttpClient] instance.
         */
        fun build(): HttpClient = CustomHttpClient.getClient(networkModel = networkModel)
    }
}

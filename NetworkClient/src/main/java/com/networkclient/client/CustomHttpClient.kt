package com.networkclient.client

import com.networkclient.models.NetworkModel
import com.networkclient.utils.jsonConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json


/**
 * Provides a custom-configured instance of [HttpClient] using the CIO engine.
 *
 * This object configures an HTTP client with various features and settings based on the provided
 * [NetworkModel]. It includes settings for default requests, timeouts, content negotiation, logging,
 * content encoding, user agent, caching, and response validation.
 */
internal object CustomHttpClient {

    /**
     * Creates and returns a custom-configured [HttpClient].
     *
     * This method uses the provided [networkModel] to configure the [HttpClient] with specific settings.
     * The configuration includes default request settings, timeouts, content negotiation, logging,
     * content encoding, user agent, caching, and response validation. It also supports HTTP request
     * retries if enabled in the [networkModel].
     *
     * @param networkModel The configuration model containing base URL, logger settings, timeout settings,
     *                     and other network-related configurations.
     * @return A configured instance of [HttpClient] with the settings specified in [networkModel].
     */
    fun getClient(
        networkModel: NetworkModel
    ) = HttpClient(CIO) {

        // Set the default URL for all requests
        defaultRequest {
            url(networkModel.baseUrl)
        }

        // Install default request headers
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        // Install HTTP timeouts
        install(HttpTimeout) {
            requestTimeoutMillis = networkModel.networkPoolingTimeout.requestTimeout
            connectTimeoutMillis = networkModel.networkPoolingTimeout.connectTimeout
            socketTimeoutMillis = networkModel.networkPoolingTimeout.socketTimeout
        }

        // Install JSON content negotiation with the specified configuration
        install(ContentNegotiation) {
            json(jsonConfiguration)
        }

        // Install logging with configurable logger and level
        install(Logging) {
            // Set up a custom logger that delegates logging messages to the logger interface specified in [networkModel]
            logger = object : Logger {
                override fun log(message: String) {
                    networkModel.logger.loggerInterface?.message(message)
                }
            }

            // Set the logging level based on the configuration in [networkModel]
            level = networkModel.logger.logLevel

            // Configure the logging for HTTP requests
            request {
                // Iterate over all interceptors and invoke the `onRequest` method for each
                networkModel.interceptors.forEach { interceptor ->
                    interceptor.onRequest(builder = this)
                }
            }
        }


        // Install content encoding with deflate and gzip
        install(ContentEncoding) {
            deflate(1.0F)
            gzip(0.9F)
        }

        // Install user agent
        install(UserAgent) {
            agent = "Network Client"
        }

        // Install HTTP caching
        install(HttpCache)

        // Install HTTP request retry logic if enabled
        if (networkModel.retryEnable) {
            install(HttpRequestRetry) {
                // Set the maximum number of retries for failed requests
                maxRetries = networkModel.retryCount

                // Define the condition under which a request should be retried
                retryIf { _, response ->
                    !response.status.isSuccess()
                }

                // Define the delay between retries
                delayMillis { retry ->
                    retry * 1000L // Delay increases with each retry attempt (e.g., 1s, 2s, 3s, ...)
                }

                // Modify the request to include the retry count in the headers
                modifyRequest { request ->
                    request.headers.append("x-retry-count", retryCount.toString())
                }
            }
        }


        // Install HTTP response validation
        HttpResponseValidator {
            validateResponse { response ->
                // Allow interceptors to handle the response
                networkModel.interceptors.forEach { interceptor ->
                    interceptor.onResponse(response = response)
                }

                // Check the HTTP status code and throw exceptions for specific error conditions
                when (response.status) {
                    HttpStatusCode.BadRequest -> throw ClientRequestException(response, "Bad request error.")
                    HttpStatusCode.InternalServerError -> throw ServerResponseException(response, "Server error.")
                    else -> if (response.status.value >= 300) {
                        // Throw a general exception for other HTTP status codes indicating errors
                        throw ResponseException(response, "Unhandled HTTP status code: ${response.status}")
                    }
                }
            }
        }

    }
}


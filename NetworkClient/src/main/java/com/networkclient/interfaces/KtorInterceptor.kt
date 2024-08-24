package com.networkclient.interfaces

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse

/**
 * Interface for intercepting HTTP requests and responses in Ktor.
 *
 * Implement this interface to customize or modify HTTP requests and responses.
 * This is useful for tasks such as logging, authentication, or modifying request/response headers.
 */
interface KtorInterceptor {

    /**
     * Called before the HTTP request is sent.
     *
     * @param builder The [HttpRequestBuilder] for the HTTP request. Modify this builder to change
     *                the request before it is sent.
     */
    fun onRequest(builder: HttpRequestBuilder)

    /**
     * Called after the HTTP response is received.
     *
     * @param response The [HttpResponse] received from the server. Use this method to inspect or
     *                 modify the response as needed.
     */
    fun onResponse(response: HttpResponse)
}

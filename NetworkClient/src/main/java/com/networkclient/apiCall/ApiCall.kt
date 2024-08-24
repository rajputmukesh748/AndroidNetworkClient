package com.networkclient.apiCall

import com.networkclient.models.ResponseCallback
import com.networkclient.utils.addRequestData
import com.networkclient.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.options
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType


/**
 * Sends a GET request to the specified URL path with optional headers and query parameters.
 *
 * @param urlPath The relative URL path to send the GET request to.
 * @param headers Optional map of headers to include in the request.
 * @param queryParams Optional map of query parameters to include in the request.
 * @return A [ResponseCallback] that contains the response or error details.
 */
suspend inline fun <reified T> HttpClient.getCall(
    urlPath: String,
    headers: Map<String, String>? = null,
    queryParams: Map<String, Any>? = null
): ResponseCallback<T> {
    return safeApiCall {
        get {
            url(urlPath)
            contentType(ContentType.Application.Json)
            addRequestData(header = headers, query = queryParams)
        }
    }
}

/**
 * Sends a POST request to the specified URL path with optional headers, query parameters, and body.
 *
 * @param urlPath The relative URL path to send the POST request to.
 * @param headers Optional map of headers to include in the request.
 * @param queryParams Optional map of query parameters to include in the request.
 * @param body Optional body of the request.
 * @return A [ResponseCallback] that contains the response or error details.
 */
suspend inline fun <reified T> HttpClient.postCall(
    urlPath: String,
    headers: Map<String, String>? = null,
    queryParams: Map<String, Any>? = null,
    body: Any?
): ResponseCallback<T> {
    return safeApiCall {
        post {
            url(urlPath)
            contentType(ContentType.Application.Json)
            addRequestData(header = headers, query = queryParams, body = body)
        }
    }
}

/**
 * Sends a PUT request to the specified URL path with optional headers, query parameters, and body.
 *
 * @param urlPath The relative URL path to send the PUT request to.
 * @param headers Optional map of headers to include in the request.
 * @param queryParams Optional map of query parameters to include in the request.
 * @param body Optional body of the request.
 * @return A [ResponseCallback] that contains the response or error details.
 */
suspend inline fun <reified T> HttpClient.putCall(
    urlPath: String,
    headers: Map<String, String>? = null,
    queryParams: Map<String, Any>? = null,
    body: Any?
): ResponseCallback<T> {
    return safeApiCall {
        put {
            url(urlPath)
            contentType(ContentType.Application.Json)
            addRequestData(header = headers, query = queryParams, body = body)
        }
    }
}

/**
 * Sends a PATCH request to the specified URL path with optional headers, query parameters, and body.
 *
 * @param urlPath The relative URL path to send the PATCH request to.
 * @param headers Optional map of headers to include in the request.
 * @param queryParams Optional map of query parameters to include in the request.
 * @param body Optional body of the request.
 * @return A [ResponseCallback] that contains the response or error details.
 */
suspend inline fun <reified T> HttpClient.patchCall(
    urlPath: String,
    headers: Map<String, String>? = null,
    queryParams: Map<String, Any>? = null,
    body: Any?
): ResponseCallback<T> {
    return safeApiCall {
        patch {
            url(urlPath)
            contentType(ContentType.Application.Json)
            addRequestData(header = headers, query = queryParams, body = body)
        }
    }
}

/**
 * Sends a DELETE request to the specified URL path with optional headers, query parameters, and body.
 *
 * @param urlPath The relative URL path to send the DELETE request to.
 * @param headers Optional map of headers to include in the request.
 * @param queryParams Optional map of query parameters to include in the request.
 * @param body Optional body of the request.
 * @return A [ResponseCallback] that contains the response or error details.
 */
suspend inline fun <reified T> HttpClient.deleteCall(
    urlPath: String,
    headers: Map<String, String>? = null,
    queryParams: Map<String, Any>? = null,
    body: Any?
): ResponseCallback<T> {
    return safeApiCall {
        delete {
            url(urlPath)
            contentType(ContentType.Application.Json)
            addRequestData(header = headers, query = queryParams, body = body)
        }
    }
}

/**
 * Sends an OPTIONS request to the specified URL path with optional headers, query parameters, and body.
 *
 * @param urlPath The relative URL path to send the OPTIONS request to.
 * @param headers Optional map of headers to include in the request.
 * @param queryParams Optional map of query parameters to include in the request.
 * @param body Optional body of the request.
 * @return A [ResponseCallback] that contains the response or error details.
 */
suspend inline fun <reified T> HttpClient.optionCall(
    urlPath: String,
    headers: Map<String, String>? = null,
    queryParams: Map<String, Any>? = null,
    body: Any?
): ResponseCallback<T> {
    return safeApiCall {
        options {
            url(urlPath)
            contentType(ContentType.Application.Json)
            addRequestData(header = headers, query = queryParams, body = body)
        }
    }
}

/**
 * Sends a HEAD request to the specified URL path with optional headers, query parameters, and body.
 *
 * @param urlPath The relative URL path to send the HEAD request to.
 * @param headers Optional map of headers to include in the request.
 * @param queryParams Optional map of query parameters to include in the request.
 * @param body Optional body of the request.
 * @return A [ResponseCallback] that contains the response or error details.
 */
suspend inline fun <reified T> HttpClient.headCall(
    urlPath: String,
    headers: Map<String, String>? = null,
    queryParams: Map<String, Any>? = null,
    body: Any?
): ResponseCallback<T> {
    return safeApiCall {
        head {
            url(urlPath)
            contentType(ContentType.Application.Json)
            addRequestData(header = headers, query = queryParams, body = body)
        }
    }
}


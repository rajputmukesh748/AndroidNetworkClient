package com.networkclient.utils

import android.webkit.MimeTypeMap
import com.networkclient.enums.LoggerLevel
import com.networkclient.models.ResponseCallback
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.encodeToString


/**
 * Extension function to convert a [LoggerLevel] into a [LogLevel].
 *
 * This function maps the custom [LoggerLevel] enum values to corresponding [LogLevel] values.
 * It is useful when you need to align different logging levels between libraries or custom implementations.
 *
 * @return The corresponding [LogLevel] based on the provided [LoggerLevel].
 */
fun LoggerLevel.getLoggerLevel(): LogLevel {
    return when (this) {
        LoggerLevel.ALL -> LogLevel.ALL
        LoggerLevel.BODY -> LogLevel.BODY
        LoggerLevel.INFO -> LogLevel.INFO
        LoggerLevel.HEADERS -> LogLevel.HEADERS
        LoggerLevel.NONE -> LogLevel.NONE
    }
}

/**
 * Extension function to add headers to an [HttpRequestBuilder].
 *
 * This function appends all key-value pairs from the provided [header] map to the HTTP request headers.
 *
 * @param header A map containing the headers to be added to the request. If null, no headers will be added.
 */
private fun HttpRequestBuilder.addHeaders(header: Map<String, String>? = null) {
    header?.forEach { (key, value) ->
        headers.append(name = key, value = value)
    }
}


/**
 * Extension function to add query parameters to an [HttpRequestBuilder].
 *
 * This function appends all key-value pairs from the provided [query] map to the HTTP request's query parameters.
 *
 * @param query A map containing the query parameters to be added to the request. If null, no query parameters will be added.
 */
private fun HttpRequestBuilder.addQueryParams(query: Map<String, Any>? = null) {
    query?.forEach { (key, value) ->
        parameter(key = key, value = value)
    }
}


/**
 * Extension function to add a body to an [HttpRequestBuilder].
 *
 * This function serializes the provided [body] to JSON and sets it as the request body. If the body is null, no action is taken.
 *
 * @param body The object to be serialized and set as the HTTP request body. If null, no body will be set.
 */
private fun HttpRequestBuilder.addBody(body: Any?) {
    body?.let { setBody(jsonConfiguration.encodeToString(body)) }
}


/**
 * Adds headers, query parameters, and a body to an [HttpRequestBuilder].
 *
 * This function consolidates the addition of headers, query parameters, and body data to an HTTP request.
 * It uses the helper functions [addHeaders], [addQueryParams], and [addBody] to streamline the process.
 *
 * @param header A map of headers to be added to the request. If null, no headers are added.
 * @param query A map of query parameters to be added to the request. If null, no query parameters are added.
 * @param body The object to be serialized and set as the request body. If null, no body is added.
 */
fun HttpRequestBuilder.addRequestData(
    header: Map<String, String>? = null,
    query: Map<String, Any>? = null,
    body: Any? = null
) {
    addHeaders(header = header)
    addQueryParams(query = query)
    addBody(body = body)
}


/**
 * Executes a safe API call using an [HttpClient] and handles the response.
 *
 * This function wraps an API call in a try-catch block to safely handle exceptions and
 * convert them into a [ResponseCallback] that represents different states like success, error, etc.
 *
 * @param apiCall A lambda function that executes the API call and returns an [HttpResponse].
 * @return A [ResponseCallback] that encapsulates the result of the API call, including success or error states.
 */
suspend inline fun <reified T> HttpClient.safeApiCall(apiCall: () -> HttpResponse): ResponseCallback<T> {
    try {
        val response = use { apiCall() }
        return response.handleResponse()
    } catch (e: Exception) {
        return e.toCustomExceptions()
    }
}


/**
 * Retrieves the MIME type of a file based on its extension.
 *
 * This function attempts to determine the MIME type of a file using its URL or file extension.
 * If the MIME type cannot be determined, it defaults to "image/jpg".
 *
 * @return The MIME type as a [String]. If the MIME type cannot be determined, "image/jpg" is returned.
 */
fun String.getMimeType(): String {
    return try {
        var mimeType = MimeTypeMap.getFileExtensionFromUrl(this.replace(" ", ""))
        if (mimeType.isNullOrEmpty())
            mimeType = this.replace(" ", "").substring(this.replace(" ", "").lastIndexOf("."))
                .replace(".", "")
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeType) ?: "image/jpg"
    } catch (e: Exception) {
        "image/jpg"
    }
}

package com.networkclient.utils

import com.networkclient.models.ResponseCallback
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * Extension function to convert an [Exception] into a custom [ResponseCallback].
 *
 * This function handles various types of network and response-related exceptions
 * and converts them into appropriate instances of [ResponseCallback]. It categorizes
 * exceptions such as no internet, timeouts, and different HTTP response errors.
 *
 * @return A [ResponseCallback] that encapsulates the error details based on the exception type.
 */
suspend fun <T> Exception.toCustomExceptions(): ResponseCallback<T> = when (this) {

    // Handle no internet connection or unknown host error
    is UnknownHostException -> ResponseCallback.NoInternetException(
        errorMessage = "Network error: No internet connection or unknown host."
    )

    // Handle request timeout error
    is SocketTimeoutException -> ResponseCallback.Error(
        errorData = "Network error: Request timed out."
    )

    // Handle IO exception
    is IOException -> ResponseCallback.Error(
        errorData = "Network error: IO exception occurred."
    )

    // Handle HTTP redirect response exception
    is RedirectResponseException -> getErrorMessage().let {
        ResponseCallback.Error(
            it.first, // HTTP status code
            it.second // Error message
        )
    }

    // Handle HTTP client request exception (e.g., 4xx responses)
    is ClientRequestException -> {
        if (response.status == HttpStatusCode.Unauthorized) getErrorMessage().let {
            ResponseCallback.SessionExpire(
                it.first, // HTTP status code
                it.second // Error message
            )
        } else getErrorMessage().let {
            ResponseCallback.Error(
                it.first, // HTTP status code
                it.second // Error message
            )
        }
    }

    // Handle HTTP server response exception (e.g., 5xx responses)
    is ServerResponseException -> getErrorMessage().let {
        ResponseCallback.SessionExpire(
            it.first, // HTTP status code
            it.second // Error message
        )
    }

    // Handle other HTTP response exceptions
    is ResponseException -> getErrorMessage().let {
        ResponseCallback.Error(
            it.first, // HTTP status code
            it.second // Error message
        )
    }

    // Handle all other exceptions
    else -> localizedMessage.orEmpty().ifEmpty { "Something went wrong." }
        .let { ResponseCallback.Error(errorData = it) }
}


/**
 * Retrieves a detailed error message from a [ResponseException].
 *
 * This function attempts to extract an error message from the response body,
 * typically from fields such as "message" or "error". If these fields are not found,
 * it defaults to using the HTTP status description.
 *
 * @return A [Pair] containing the HTTP status code and the extracted error message.
 */
private suspend fun ResponseException.getErrorMessage(): Pair<Int, String> {
    // Read the response body as a string
    val errorString = response.body<String>()
    // Parse the response body as a JSON object
    val jsonObject = JSONObject(errorString)
    return when {
        // Extract "message" field if available
        jsonObject.has("message") -> Pair(
            response.status.value, // HTTP status code
            jsonObject.getString("message") // Error message
        )
        // Extract "error" field if available
        jsonObject.has("error") -> Pair(
            response.status.value, // HTTP status code
            jsonObject.getString("error") // Error message
        )
        // Default to the HTTP status description if no specific error message is found
        else -> Pair(response.status.value, response.status.description)
    }
}


package com.networkclient.utils

import com.networkclient.models.ResponseCallback
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import io.ktor.client.statement.*
import io.ktor.http.*


/**
 * Extension function to handle the [HttpResponse] and convert it into a [ResponseCallback].
 *
 * This function processes the [HttpResponse] received from an API call. It checks if the HTTP status
 * indicates success and returns a [ResponseCallback.Success] with the response body.
 * If the status indicates an error, it returns a [ResponseCallback.Error] with the error body.
 *
 * @return A [ResponseCallback] that represents either a successful or failed response.
 */
suspend inline fun <reified T> HttpResponse.handleResponse(): ResponseCallback<T> {
    return if (this.status.isSuccess()) {
        ResponseCallback.Success(this.status.value, this.body<T>())
    } else {
        ResponseCallback.Error(this.status.value, this.body())
    }
}

/**
 * JSON configuration settings for Kotlinx Serialization.
 *
 * This configuration enables pretty printing, leniency, encoding of default values,
 * and the ability to ignore unknown keys when deserializing JSON objects.
 */
val jsonConfiguration = Json {
    prettyPrint = true
    isLenient = true
    encodeDefaults = true
    ignoreUnknownKeys = true
}

/**
 * Converts any object into a JSON string.
 *
 * This extension function serializes the object it is called on into a JSON string
 * using the predefined [jsonConfiguration].
 *
 * @return A JSON [String] representation of the object.
 */
fun Any.toJson() = jsonConfiguration.encodeToString(this)

/**
 * Converts a JSON string into a model class of type [T].
 *
 * This inline function deserializes a JSON string into an instance of the specified type [T].
 * The deserialization process is based on the [jsonConfiguration], which handles various
 * settings such as ignoring unknown keys and lenient parsing.
 *
 * @return An instance of type [T] created from the JSON string.
 * @throws SerializationException if the JSON cannot be parsed into the specified type [T].
 */
inline fun <reified T> String.fromJson(): T = jsonConfiguration.decodeFromString(this)

package com.networkclient.utils

import com.networkclient.models.ResponseCallback
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart


/**
 * Converts a [ResponseCallback] instance into a Flow.
 *
 * This extension function takes an instance of [ResponseCallback] and emits it as a Flow.
 * The Flow operates on the provided [CoroutineDispatcher] or defaults to [Dispatchers.IO].
 *
 * @param dispatcher The [CoroutineDispatcher] on which the Flow should operate. If null, defaults to [Dispatchers.IO].
 * @return A [Flow] that emits the current [ResponseCallback] instance.
 */
fun <T> ResponseCallback<T?>.convertToFlow(dispatcher: CoroutineDispatcher? = null) = flow {
    emit(this@convertToFlow)
}.flowOn(dispatcher ?: Dispatchers.IO)



/**
 * Collects and processes API data from a Flow of [ResponseCallback].
 *
 * This suspending function collects the emissions from a Flow of [ResponseCallback] and invokes corresponding
 * callback functions based on the type of [ResponseCallback]. The function handles different states such as loading,
 * success, error, no internet connection, and session expiration.
 *
 * @param onLoading A callback function to be invoked when the request is in the loading state.
 * @param onSuccess A callback function to be invoked when the request is successful. It receives the body of the response
 *                  and the HTTP status code as parameters.
 * @param onError A callback function to be invoked when an error occurs. It receives the error data or message.
 * @param noInternet A callback function to be invoked when there is no internet connection. It receives a message describing the issue.
 * @param onSessionExpire A callback function to be invoked when the session has expired. It receives a message indicating the session expiration.
 *
 * @throws Exception If an unhandled exception occurs during Flow collection, it will be caught, and the [onError] callback will be invoked.
 */
suspend fun <T> Flow<ResponseCallback<T?>>.collectApiData(
    onLoading: () -> Unit,
    onSuccess: T?.(statusCode: Int) -> Unit,
    onError: Any.() -> Unit,
    noInternet: String.() -> Unit,
    onSessionExpire: String.() -> Unit
) {
    try {
        // Starts the Flow collection, invoking the onLoading callback.
        this.onStart {
            onLoading.invoke()
        }
            // Catches any exception during Flow collection and invokes the onError callback.
            .catch { error ->
                onError.invoke(error.localizedMessage.orEmpty())
            }
            // Collects each emitted ResponseCallback and invokes the appropriate callback based on its type.
            .collect { response ->
                when (response) {
                    is ResponseCallback.Success -> {
                        onSuccess.invoke(response.body, response.code)
                    }
                    is ResponseCallback.Error -> {
                        onError.invoke(response.errorData)
                    }
                    is ResponseCallback.NoInternetException -> {
                        noInternet.invoke(response.errorMessage.toString())
                    }
                    is ResponseCallback.SessionExpire -> {
                        onSessionExpire.invoke(response.errorMessage)
                    }
                    else -> Unit // Handles any other cases that don't match the known types.
                }
            }
    } catch (e: Exception) {
        // Catches any unhandled exception and invokes the onError callback with the exception message.
        onError.invoke(e.localizedMessage.orEmpty())
    }
}

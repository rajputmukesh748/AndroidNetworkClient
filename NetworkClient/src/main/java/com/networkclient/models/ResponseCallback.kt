package com.networkclient.models

import androidx.annotation.Keep

/**
 * A sealed class representing the different states of a network response.
 *
 * This class encapsulates all possible outcomes of a network request, providing a type-safe way
 * to handle success, error, and other exceptional cases.
 *
 * @param T The type of the data expected in the response.
 */
@Keep
sealed class ResponseCallback<T> {

    /**
     * Represents a loading state while the network request is being processed.
     *
     * This class does not hold any data and is typically used to indicate that a request is ongoing.
     */
    data object OnLoading : ResponseCallback<Nothing>()


    /**
     * Represents a successful network response.
     *
     * @param T The type of the data expected in the response.
     * @property code The HTTP status code of the response.
     * @property body The data returned by the request, which may be null.
     */
    data class Success<T>(
        val code: Int,
        val body: T?
    ) : ResponseCallback<T>()


    /**
     * Represents an error response from the network request.
     *
     * @param T The type of the data expected in the response.
     * @property code The HTTP status code of the error, which may be null.
     * @property errorData Any additional data or message related to the error.
     */
    data class Error<T>(
        val code: Int? = null,
        val errorData: Any
    ) : ResponseCallback<T>()


    /**
     * Represents a situation where the network request could not be completed due to no internet connection.
     *
     * @param T The type of the data expected in the response.
     * @property errorMessage A message or object describing the lack of internet connection.
     */
    data class NoInternetException<T>(
        val errorMessage: String
    ) : ResponseCallback<T>()


    /**
     * Represents a situation where the user's session has expired.
     *
     * @param T The type of the data expected in the response.
     * @property code The code indicating the session expiration status.
     * @property errorMessage A message indicating that the session has expired.
     */
    data class SessionExpire<T>(
        val code: Int,
        val errorMessage: String
    ) : ResponseCallback<T>()
}

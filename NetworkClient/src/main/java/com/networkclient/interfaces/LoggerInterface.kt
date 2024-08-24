package com.networkclient.interfaces

/**
 * A functional interface for custom logging behavior.
 *
 * This interface allows you to define custom logging logic by implementing the `message` function.
 * It can be used to log messages in a specific format, send logs to a remote server, or handle logging
 * in any way that suits your application's needs. Being a functional interface, it can be implemented
 * using a lambda expression.
 *
 * @Example_Usage:
 * ```
 * val customLogger = LoggerInterface { logMessage ->
 *     println("Custom Log: $logMessage")
 * }
 * customLogger.message("This is a log message")
 * ```
 */
fun interface LoggerInterface {
    /**
     * Logs a message.
     *
     * @param string The message to be logged. This can be any string that represents a log message, such as an error message,
     * debug information, or any other relevant details.
     */
    fun message(string: String)
}

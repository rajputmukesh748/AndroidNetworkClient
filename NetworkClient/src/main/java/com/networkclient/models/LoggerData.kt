package com.networkclient.models

import androidx.annotation.Keep
import com.networkclient.interfaces.LoggerInterface
import io.ktor.client.plugins.logging.LogLevel

/**
 * A data class representing the configuration for logging.
 *
 * This class encapsulates the logging level and the interface for the logger, allowing
 * you to specify how logging should be handled within your application.
 *
 * @property logLevel The level of logging to be performed. Default is [LogLevel.ALL], meaning all logs will be captured.
 * @property loggerInterface An optional interface to define custom logging behavior. If null, default logging is used.
 */
@Keep
data class LoggerData(
    val logLevel: LogLevel = LogLevel.ALL,
    val loggerInterface: LoggerInterface? = null
)

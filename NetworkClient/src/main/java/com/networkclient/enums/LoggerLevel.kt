package com.networkclient.enums

import androidx.annotation.Keep


/**
 * Enum class representing different levels of logging.
 *
 * This enum defines the various logging levels that can be used to control the verbosity of log messages.
 * Each level specifies the type of log information that should be captured, from all logs to no logs at all.
 *
 * @property ALL Captures all types of log messages.
 * @property HEADERS Captures only header-related log messages.
 * @property BODY Captures only body-related log messages.
 * @property INFO Captures only informational log messages.
 * @property NONE Captures no log messages.
 */
@Keep
enum class LoggerLevel {
    /**
     * Logs all types of messages, including headers, body, and informational messages.
     */
    ALL,

    /**
     * Logs only header-related messages, typically used to debug or analyze HTTP headers.
     */
    HEADERS,

    /**
     * Logs only body-related messages, typically used to view the content of HTTP requests and responses.
     */
    BODY,

    /**
     * Logs only informational messages, such as general operational or status information.
     */
    INFO,

    /**
     * Disables logging entirely, capturing no log messages.
     */
    NONE
}


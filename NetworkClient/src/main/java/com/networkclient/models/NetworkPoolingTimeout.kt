package com.networkclient.models

import androidx.annotation.Keep


/**
 * A data class representing the timeout settings for network pooling operations.
 *
 * This class encapsulates the various timeout configurations for network requests, including
 * the request timeout, connection timeout, and socket timeout. These settings determine how
 * long the network client will wait during different stages of a network operation before
 * considering it a failure due to timeout.
 *
 * @property requestTimeout The maximum time (in milliseconds) the client will wait for a full request to complete. Default is 3000ms.
 * @property connectTimeout The maximum time (in milliseconds) the client will wait to establish a connection to the server. Default is 3000ms.
 * @property socketTimeout The maximum time (in milliseconds) the client will wait for data on a socket. Default is 3000ms.
 */
@Keep
data class NetworkPoolingTimeout(
    var requestTimeout: Long = 3000,
    var connectTimeout: Long = 3000,
    var socketTimeout: Long = 3000
)

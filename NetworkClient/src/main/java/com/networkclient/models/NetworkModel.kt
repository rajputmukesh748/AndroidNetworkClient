package com.networkclient.models

import androidx.annotation.Keep
import com.networkclient.interfaces.KtorInterceptor
import java.util.LinkedList


/**
 * Data class representing the configuration for network operations.
 *
 * @property baseUrl The base URL for network requests. This is the root URL that will be used for
 *                   all network requests.
 * @property logger An instance of [LoggerData] to handle logging for network operations. This allows
 *                  you to configure logging behavior such as log levels and formats.
 * @property networkPoolingTimeout Configuration for network pooling timeouts through an instance of
 *                                  [NetworkPoolingTimeout]. This specifies the timeout settings for
 *                                  network connections.
 * @property interceptors A list of [KtorInterceptor] instances used to intercept and modify HTTP
 *                         requests and responses. This allows for custom behaviors such as logging,
 *                         authentication, or header manipulation.
 * @property retryEnable A boolean flag indicating whether automatic retry is enabled for network
 *                       requests. When set to true, failed requests will be retried according to the
 *                       [retryCount].
 * @property retryCount The number of times a failed request should be retried. This value is only
 *                       relevant if [retryEnable] is set to true.
 */
@Keep
data class NetworkModel(
    var baseUrl: String,
    var logger: LoggerData = LoggerData(),
    var networkPoolingTimeout: NetworkPoolingTimeout = NetworkPoolingTimeout(),
    var interceptors: LinkedList<KtorInterceptor> = LinkedList(),
    var retryEnable: Boolean = false,
    var retryCount: Int = 0
)

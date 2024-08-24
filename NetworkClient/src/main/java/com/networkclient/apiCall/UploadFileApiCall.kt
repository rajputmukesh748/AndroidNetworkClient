package com.networkclient.apiCall

import com.networkclient.models.ResponseCallback
import com.networkclient.utils.addRequestData
import com.networkclient.utils.getMimeType
import com.networkclient.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import io.ktor.client.request.*


/**
 * Sends a POST request with multipart form data to the specified URL path.
 *
 * @param urlPath The relative URL path for the request.
 * @param headers Optional map of headers to include in the request.
 * @param queryParams Optional map of query parameters to include in the request.
 * @param body Optional map of form data to include in the request body.
 * @param fileMap Map of file parameters to include in the multipart request.
 * @return A [ResponseCallback] containing the result of the request or error details.
 */
suspend inline fun <reified T> HttpClient.postCall(
    urlPath: String,
    headers: HashMap<String, String>? = null,
    queryParams: HashMap<String, Any>? = null,
    body: HashMap<String, Any>? = null,
    fileMap: HashMap<String, File>
): ResponseCallback<T> {
    return safeApiCall {
        post {
            createMultipartRequest(urlPath, fileMap, headers, queryParams, body)
        }
    }
}


/**
 * Sends a PUT request with multipart form data to the specified URL path.
 *
 * @param urlPath The relative URL path for the request.
 * @param headers Optional map of headers to include in the request.
 * @param queryParams Optional map of query parameters to include in the request.
 * @param body Optional map of form data to include in the request body.
 * @param fileMap Map of file parameters to include in the multipart request.
 * @return A [ResponseCallback] containing the result of the request or error details.
 */
suspend inline fun <reified T> HttpClient.putCall(
    urlPath: String,
    headers: HashMap<String, String>? = null,
    queryParams: HashMap<String, Any>? = null,
    body: HashMap<String, Any>? = null,
    fileMap: HashMap<String, File>
): ResponseCallback<T> {
    return safeApiCall {
        put {
            createMultipartRequest(urlPath, fileMap, headers, queryParams, body)
        }
    }
}


/**
 * Sends a PATCH request with multipart form data to the specified URL path.
 *
 * @param urlPath The relative URL path for the request.
 * @param headers Optional map of headers to include in the request.
 * @param queryParams Optional map of query parameters to include in the request.
 * @param body Optional map of form data to include in the request body.
 * @param fileMap Map of file parameters to include in the multipart request.
 * @return A [ResponseCallback] containing the result of the request or error details.
 */
suspend inline fun <reified T> HttpClient.patchCall(
    urlPath: String,
    headers: HashMap<String, String>? = null,
    queryParams: HashMap<String, Any>? = null,
    body: HashMap<String, Any>? = null,
    fileMap: HashMap<String, File>
): ResponseCallback<T> {
    return safeApiCall {
        patch {
            createMultipartRequest(urlPath, fileMap, headers, queryParams, body)
        }
    }
}


/**
 * Configures a multipart request with form data and files.
 *
 * @param urlPath The relative URL path for the request.
 * @param fileMap Map of file parameters to include in the multipart request.
 * @param headers Optional map of headers to include in the request.
 * @param queryParams Optional map of query parameters to include in the request.
 * @param body Optional map of form data to include in the request body.
 */
fun HttpRequestBuilder.createMultipartRequest(
    urlPath: String,
    fileMap: Map<String, File>,
    headers: Map<String, String>? = null,
    queryParams: Map<String, Any>? = null,
    body: Map<String, Any>? = null
) {
    url(urlPath)
    addRequestData(header = headers, query = queryParams)
    setBody(MultiPartFormDataContent(
        parts = formData {
            fileMap.forEach { (key, file) ->
                append(key, file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, file.path.getMimeType())
                    append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                })
            }
            body?.forEach { (key, value) ->
                append(key, value.toString())
            }
        }
    ))
}
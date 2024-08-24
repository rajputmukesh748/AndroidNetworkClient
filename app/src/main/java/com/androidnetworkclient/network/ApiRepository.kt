package com.androidnetworkclient.network

import com.androidnetworkclient.model.Post
import com.networkclient.apiCall.getCall
import com.networkclient.models.ResponseCallback

object ApiRepository {

    suspend fun getApi(): ResponseCallback<List<Post>> = ApiClient.getClient().getCall<List<Post>>(
        urlPath = "products",
    )

}
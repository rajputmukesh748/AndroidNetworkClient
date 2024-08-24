package com.androidnetworkclient.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.*

@Serializable
data class Post(
    @SerialName("userId")
    val userId: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("body")
    val body: String? = null,
)

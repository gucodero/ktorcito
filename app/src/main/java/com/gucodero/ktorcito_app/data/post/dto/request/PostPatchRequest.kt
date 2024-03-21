package com.gucodero.ktorcito_app.data.post.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PostPatchRequest(
    @SerialName("userId")
    val userId: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("body")
    val body: String? = null
)
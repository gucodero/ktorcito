package com.gucodero.ktorcito_app.data.post.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PostIdResponse(
    @SerialName("id")
    val id: Int
)
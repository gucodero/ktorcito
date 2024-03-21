package com.gucodero.ktorcito_app.data.post.mapper

import com.gucodero.ktorcito_app.data.post.dto.response.PostResponse
import com.gucodero.ktorcito_app.domain.post.entity.Post

fun PostResponse.toDomain(): Post = Post(
    userId = userId,
    id = id,
    title = title,
    body = body
)
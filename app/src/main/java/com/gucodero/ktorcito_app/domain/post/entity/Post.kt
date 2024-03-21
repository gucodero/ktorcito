package com.gucodero.ktorcito_app.domain.post.entity

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
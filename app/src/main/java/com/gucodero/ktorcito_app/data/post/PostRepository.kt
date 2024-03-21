package com.gucodero.ktorcito_app.data.post

import com.gucodero.ktorcito.Response
import com.gucodero.ktorcito_app.domain.post.entity.Post

interface PostRepository {

    suspend fun getPosts(
        userId: Int? = null
    ): Response<List<Post>>

    suspend fun getPostById(id: Int): Response<Post?>

    suspend fun createPost(
        userId: Int,
        title: String,
        body: String
    ): Response<Post>

    suspend fun updatePost(
        id: Int,
        userId: Int,
        title: String,
        body: String
    ): Response<Post>

    suspend fun patchPost(
        id: Int,
        userId: Int? = null,
        title: String? = null,
        body: String? = null
    ): Response<Post>

    suspend fun deletePost(id: Int): Response<Unit>

}
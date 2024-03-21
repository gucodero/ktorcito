package com.gucodero.ktorcito_app.data.post.remote

import com.gucodero.ktorcito_app.data.post.dto.response.PostResponse
import com.gucodero.ktorcito.Response
import com.gucodero.ktorcito_app.data.post.dto.request.PostPatchRequest
import com.gucodero.ktorcito_app.data.post.dto.request.PostRequest

interface PostApi {

    suspend fun getPosts(
        userId: Int? = null
    ): Response<List<PostResponse>>

    suspend fun getPostById(id: Int): Response<PostResponse?>

    suspend fun createPost(post: PostRequest): Response<PostResponse>

    suspend fun updatePost(
        id: Int,
        post: PostRequest
    ): Response<PostResponse>

    suspend fun patchPost(
        id: Int,
        post: PostPatchRequest
    ): Response<PostResponse>

    suspend fun deletePost(id: Int): Response<Unit>

}
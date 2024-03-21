package com.gucodero.ktorcito_app.data.post.remote

import com.gucodero.ktorcito_app.data.post.dto.response.PostResponse
import com.gucodero.ktorcito_app.data.common.util.Response
import com.gucodero.ktorcito_app.data.post.dto.request.PostPatchRequest
import com.gucodero.ktorcito_app.data.post.dto.request.PostRequest

interface PostApi {

    suspend fun getPosts(
        userId: Int? = null
    ): Response<List<PostResponse>>

    suspend fun getPostById(id: Int): Response<PostResponse?>

    suspend fun createPost(placeholder: PostRequest): Response<PostResponse>

    suspend fun updatePost(
        id: Int,
        placeholder: PostRequest
    ): Response<PostResponse>

    suspend fun patchPost(
        id: Int,
        placeholder: PostPatchRequest
    ): Response<PostResponse>

    suspend fun deletePost(id: Int): Response<Unit>

}
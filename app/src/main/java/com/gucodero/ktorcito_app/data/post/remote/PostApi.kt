package com.gucodero.ktorcito_app.data.post.remote

import com.gucodero.ktorcito_app.data.post.dto.response.PostResponse
import com.gucodero.ktorcito.Response
import com.gucodero.ktorcito.annotations.Controller
import com.gucodero.ktorcito.annotations.Body
import com.gucodero.ktorcito.annotations.Delete
import com.gucodero.ktorcito.annotations.Get
import com.gucodero.ktorcito.annotations.Patch
import com.gucodero.ktorcito.annotations.Path
import com.gucodero.ktorcito.annotations.Post
import com.gucodero.ktorcito.annotations.Put
import com.gucodero.ktorcito.annotations.Query
import com.gucodero.ktorcito_app.data.post.dto.request.PostPatchRequest
import com.gucodero.ktorcito_app.data.post.dto.request.PostRequest

@Controller
interface PostApi {

    @Get("posts")
    suspend fun getPosts(
        @Query("userId") userId: Int? = null
    ): Response<List<PostResponse>>

    @Get("posts/{id}")
    suspend fun getPostById(
        @Path("id") id: Int
    ): Response<PostResponse?>

    @Post("posts")
    suspend fun createPost(
        @Body post: PostRequest
    ): Response<PostResponse>

    @Put("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body post: PostRequest
    ): Response<PostResponse>

    @Patch("posts/{id}")
    suspend fun patchPost(
        @Path("id") id: Int,
        @Body post: PostPatchRequest
    ): Response<PostResponse>


    @Delete("posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Int
    ): Response<Unit>
}
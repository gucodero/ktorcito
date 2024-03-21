package com.gucodero.ktorcito_app.data.post.remote

import com.gucodero.ktorcito.KtorcitoURL
import com.gucodero.ktorcito_app.data.post.dto.response.PostResponse
import com.gucodero.ktorcito.Response
import com.gucodero.ktorcito.jsonBody
import com.gucodero.ktorcito.setJsonBody
import com.gucodero.ktorcito_app.data.post.dto.request.PostPatchRequest
import com.gucodero.ktorcito_app.data.post.dto.request.PostRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostApiImpl @Inject constructor(
    private val baseUrl: String,
    private val httpClient: HttpClient
): PostApi {

    override suspend fun getPosts(
        userId: Int?
    ): Response<List<PostResponse>> = withContext(Dispatchers.IO) {
        val ktorcitoURL: KtorcitoURL = KtorcitoURL.Builder()
            .baseUrl(baseUrl)
            .endpoint("posts")
            .query("userId", userId)
            .build()
        val result: HttpResponse = httpClient.get(
            urlString = ktorcitoURL.url
        )
        Response(
            httpResponse = result,
            body = result.jsonBody()
        )
    }

    override suspend fun getPostById(id: Int): Response<PostResponse?> = withContext(Dispatchers.IO)  {
        val ktorcitoURL: KtorcitoURL = KtorcitoURL.Builder()
            .baseUrl(baseUrl)
            .endpoint("posts/{id}")
            .path("id", id)
            .build()
        val result: HttpResponse = httpClient.get(
            urlString = ktorcitoURL.url
        )
        Response(
            httpResponse = result,
            body = result.jsonBody()
        )
    }

    override suspend fun createPost(
        post: PostRequest
    ): Response<PostResponse> = withContext(Dispatchers.IO) {
        val ktorcitoURL: KtorcitoURL = KtorcitoURL.Builder()
            .baseUrl(baseUrl)
            .endpoint("posts")
            .build()
        val result: HttpResponse = httpClient.post(
            urlString = ktorcitoURL.url
        ) {
            setJsonBody(body = post)
        }
        Response(
            httpResponse = result,
            body = result.jsonBody()
        )
    }

    override suspend fun updatePost(
        id: Int,
        post: PostRequest
    ): Response<PostResponse> = withContext(Dispatchers.IO) {
        val ktorcitoURL: KtorcitoURL = KtorcitoURL.Builder()
            .baseUrl(baseUrl)
            .endpoint("posts/{id}")
            .path("id", id)
            .build()
        val result: HttpResponse = httpClient.put(
            urlString = ktorcitoURL.url
        ) {
            setJsonBody(body = post)
        }
        Response(
            httpResponse = result,
            body = result.jsonBody()
        )
    }

    override suspend fun patchPost(
        id: Int,
        post: PostPatchRequest
    ): Response<PostResponse> = withContext(Dispatchers.IO) {
        val ktorcitoURL: KtorcitoURL = KtorcitoURL.Builder()
            .baseUrl(baseUrl)
            .endpoint("posts/{id}")
            .path("id", id)
            .build()
        val result: HttpResponse = httpClient.patch(
            urlString = ktorcitoURL.url
        ) {
            setJsonBody(body = post)
        }
        Response(
            httpResponse = result,
            body = result.jsonBody()
        )
    }

    override suspend fun deletePost(id: Int): Response<Unit> = withContext(Dispatchers.IO) {
        val ktorcitoURL: KtorcitoURL = KtorcitoURL.Builder()
            .baseUrl(baseUrl)
            .endpoint("posts/{id}")
            .path("id", id)
            .build()
        val result: HttpResponse = httpClient.delete(
            urlString = ktorcitoURL.url
        )
        Response(
            httpResponse = result,
            body = Unit
        )
    }

}
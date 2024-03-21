package com.gucodero.ktorcito_app.data.post.remote

import com.gucodero.ktorcito_app.data.post.dto.response.PostResponse
import com.gucodero.ktorcito_app.data.common.util.Response
import com.gucodero.ktorcito_app.data.post.dto.request.PostPatchRequest
import com.gucodero.ktorcito_app.data.post.dto.request.PostRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PostApiImpl @Inject constructor(
    private val client: HttpClient
): PostApi {

    override suspend fun getPosts(
        userId: Int?
    ): Response<List<PostResponse>> = withContext(Dispatchers.IO) {
        val querySegment: String = buildList {
            userId?.let { add("userId=$it") }
        }.joinToString("&").let {
            if (it.isNotEmpty()) {
                "?$it"
            } else {
                ""
            }
        }
        val result: HttpResponse = client.get(
            urlString = "https://jsonplaceholder.typicode.com/posts$querySegment"
        )
        val resultString: String = result.body()
        Response(
            httpResponse = result,
            body = Json.decodeFromString(resultString)
        )
    }

    override suspend fun getPostById(id: Int): Response<PostResponse?> = withContext(Dispatchers.IO)  {
        val result: HttpResponse = client.get(
            urlString = "https://jsonplaceholder.typicode.com/posts/$id"
        )
        val resultString: String = result.body()
        Response(
            httpResponse = result,
            body = Json.decodeFromString(resultString)
        )
    }

    override suspend fun createPost(
        placeholder: PostRequest
    ): Response<PostResponse> = withContext(Dispatchers.IO) {
        val result: HttpResponse = client.post(
            urlString = "https://jsonplaceholder.typicode.com/posts"
        ) {
            setBody(
                body = Json.encodeToString(placeholder)
            )
            contentType(ContentType.Application.Json)
        }
        val resultString: String = result.body()
        Response(
            httpResponse = result,
            body = Json.decodeFromString(resultString)
        )
    }

    override suspend fun updatePost(
        id: Int,
        placeholder: PostRequest
    ): Response<PostResponse> = withContext(Dispatchers.IO) {
        val result: HttpResponse = client.put(
            urlString = "https://jsonplaceholder.typicode.com/posts/$id"
        ) {
            setBody(
                body = Json.encodeToString(placeholder)
            )
            contentType(ContentType.Application.Json)
        }
        val resultString: String = result.body()
        Response(
            httpResponse = result,
            body = Json.decodeFromString(resultString)
        )
    }

    override suspend fun patchPost(
        id: Int,
        placeholder: PostPatchRequest
    ): Response<PostResponse> = withContext(Dispatchers.IO) {
        val result: HttpResponse = client.patch(
            urlString = "https://jsonplaceholder.typicode.com/posts/$id"
        ) {
            setBody(
                body = Json.encodeToString(placeholder)
            )
            contentType(ContentType.Application.Json)
        }
        val resultString: String = result.body()
        Response(
            httpResponse = result,
            body = Json.decodeFromString(resultString)
        )
    }

    override suspend fun deletePost(id: Int): Response<Unit> = withContext(Dispatchers.IO) {
        val result: HttpResponse = client.delete(
            urlString = "https://jsonplaceholder.typicode.com/posts/$id"
        )
        Response(
            httpResponse = result,
            body = Unit
        )
    }

}
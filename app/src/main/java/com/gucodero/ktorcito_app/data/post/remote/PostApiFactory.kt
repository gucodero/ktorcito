package com.gucodero.ktorcito_app.data.post.remote

import com.gucodero.ktorcito.KtorcitoFactory
import io.ktor.client.HttpClient

object PostApiFactory: KtorcitoFactory<PostApi>() {

    override fun create(
        baseUrl: String,
        httpClient: HttpClient
    ): PostApi {
        return PostApiImpl(
            baseUrl = baseUrl,
            httpClient = httpClient
        )
    }

}
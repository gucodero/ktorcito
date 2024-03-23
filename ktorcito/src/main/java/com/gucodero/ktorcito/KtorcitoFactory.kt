package com.gucodero.ktorcito

import io.ktor.client.HttpClient

interface KtorcitoFactory<T> {

    fun create(
        baseUrl: String,
        httpClient: HttpClient
    ): T

}
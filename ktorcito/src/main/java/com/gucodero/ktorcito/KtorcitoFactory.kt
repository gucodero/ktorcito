package com.gucodero.ktorcito

import io.ktor.client.HttpClient

abstract class KtorcitoFactory<T> {

    abstract fun create(
        baseUrl: String,
        httpClient: HttpClient
    ): T

}
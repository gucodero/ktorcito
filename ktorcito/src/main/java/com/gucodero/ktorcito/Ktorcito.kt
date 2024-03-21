package com.gucodero.ktorcito

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class Ktorcito private constructor(
    val baseUrl: String,
    val httpClient: HttpClient
) {

    fun <T> create(factory: KtorcitoFactory<T>): T = factory.create(baseUrl, httpClient)

    class Builder {
        private var baseUrl: String = ""
        private var httpClient: HttpClient? = null

        fun baseUrl(baseUrl: String) = apply {
            this.baseUrl = baseUrl.normalizeBaseUrl()
        }

        fun httpClient(httpClient: HttpClient) = apply { this.httpClient = httpClient }

        fun build() = Ktorcito(
            baseUrl = baseUrl,
            httpClient = httpClient ?: HttpClient(CIO)
        )
    }

}
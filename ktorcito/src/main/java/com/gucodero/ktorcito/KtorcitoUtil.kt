package com.gucodero.ktorcito

import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun String.normalizeBaseUrl(): String = if (this.endsWith("/")) {
    this.dropLast(1)
} else {
    this
}

suspend inline fun <reified T> HttpResponse.jsonBody(): T = Json.decodeFromString(body())

inline fun <reified T> HttpRequestBuilder.setJsonBody(
    body: T
) {
    body?.let {
        setBody(
            body = Json.encodeToString(body)
        )
        contentType(ContentType.Application.Json)
    }
}
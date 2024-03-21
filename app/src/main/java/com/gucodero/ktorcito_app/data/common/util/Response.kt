package com.gucodero.ktorcito_app.data.common.util

import io.ktor.client.statement.HttpResponse

@Suppress("UNCHECKED_CAST")
class Response<T>(
    val httpResponse: HttpResponse,
    private val body: T? = null
) {

    val data get() = body as T

    val isSuccessful = httpResponse.status.value in 200..299

}

inline fun <I, O> Response<I>.transform(mapper: (I) -> O): Response<O> {
    return Response(
        httpResponse = this.httpResponse,
        body = this.data?.let { mapper(it) }
    )
}

inline fun <I, O> Response<List<I>>.map(mapper: (I) -> O): Response<List<O>> {
    return Response(
        httpResponse = this.httpResponse,
        body = this.data.map(mapper)
    )
}
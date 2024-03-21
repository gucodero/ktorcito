package com.gucodero.ktorcito

class KtorcitoURL private constructor(
    val baseUrl: String,
    val endpoint: String,
    val queryParams: Map<String, String> = emptyMap(),
    val pathParams: Map<String, String> = emptyMap()
) {

    private val urlSegment: String = createURLSegment()
    private val querySegment: String = createQuerySegment()
    val url: String = createFullPath()

    private fun createURLSegment(): String {
        if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
            return endpoint
        }
        if (baseUrl.isEmpty()) {
            throw IllegalArgumentException("baseUrl cannot be empty")
        }
        val urlModel: String = if (endpoint.startsWith("/")) {
            "$baseUrl$endpoint"
        } else {
            "$baseUrl/$endpoint"
        }
        return pathParams.entries.fold(urlModel) { acc, (key: String, value: String) ->
            acc.replace("{$key}", value)
        }
    }

    private fun createQuerySegment(): String {
        if (queryParams.isEmpty()) {
            return urlSegment
        }
        return queryParams.map { (key: String, value: String) ->
            "$key=$value"
        }.joinToString("&")
    }

    private fun createFullPath(): String {
        if (urlSegment.contains("?")) {
            return "$urlSegment&$querySegment"
        }
        return "$urlSegment?$querySegment"
    }

    class Builder {
        private var baseUrl: String = ""
        private var endpoint: String = ""
        private val queryParams: MutableMap<String, String> = mutableMapOf()
        private val pathParams: MutableMap<String, String> = mutableMapOf()

        fun baseUrl(baseUrl: String) = apply {
            this.baseUrl = baseUrl.normalizeBaseUrl()
        }

        fun endpoint(endpoint: String) = apply { this.endpoint = endpoint }

        fun query(key: String, value: Any?) = apply {
            if (value != null) {
                queryParams[key] = value.toString()
            } else {
                queryParams.remove(key)
            }
        }

        fun path(key: String, value: Any?) = apply {
            if (value != null) {
                pathParams[key] = value.toString()
            } else {
                pathParams.remove(key)
            }
        }

        fun build() = KtorcitoURL(
            baseUrl = baseUrl,
            endpoint = endpoint,
            queryParams = queryParams,
            pathParams = pathParams
        )
    }

}
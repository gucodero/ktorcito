package com.gucodero.ktorcito_compiler.entity

internal enum class EndpointType(
    val funcName: String,
    val annotationName: String
) {
    GET(
        funcName = "get",
        annotationName = "Get"
    ),
    POST(
        funcName = "post",
        annotationName = "Post"
    ),
    PUT(
        funcName = "put",
        annotationName = "Put"
    ),
    DELETE(
        funcName = "delete",
        annotationName = "Delete"
    ),
    PATCH(
        funcName = "patch",
        annotationName = "Patch"
    );
    companion object {
        fun findByAnnotationName(name: String): EndpointType? {
            return entries.find { it.annotationName == name }
        }
    }
}
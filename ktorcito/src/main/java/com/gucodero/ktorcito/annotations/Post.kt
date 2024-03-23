package com.gucodero.ktorcito.annotations

@Target(AnnotationTarget.FUNCTION)
annotation class Post(
    val path: String
)

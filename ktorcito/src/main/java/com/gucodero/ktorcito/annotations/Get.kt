package com.gucodero.ktorcito.annotations

@Target(AnnotationTarget.FUNCTION)
annotation class Get(
    val path: String
)

package com.gucodero.ktorcito.annotations

@Target(AnnotationTarget.FUNCTION)
annotation class Patch(
    val path: String
)

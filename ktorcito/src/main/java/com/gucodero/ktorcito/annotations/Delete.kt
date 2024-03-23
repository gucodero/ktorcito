package com.gucodero.ktorcito.annotations

@Target(AnnotationTarget.FUNCTION)
annotation class Delete(
    val path: String
)
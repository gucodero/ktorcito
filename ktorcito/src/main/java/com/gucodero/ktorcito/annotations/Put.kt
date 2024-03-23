package com.gucodero.ktorcito.annotations

@Target(AnnotationTarget.FUNCTION)
annotation class Put(
    val path: String
)
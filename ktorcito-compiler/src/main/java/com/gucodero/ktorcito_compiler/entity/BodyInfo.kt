package com.gucodero.ktorcito_compiler.entity

class BodyInfo(
    private val parameterName: String = ""
) {
    override fun toString(): String {
        return "setJsonBody(body = $parameterName)"
    }
}
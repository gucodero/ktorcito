package com.gucodero.ktorcito_compiler.entity

import com.google.devtools.ksp.symbol.KSValueParameter
import com.gucodero.ktorcito.annotations.Query
import com.gucodero.ktorcito_compiler.util.getAnnotationOfType
import com.gucodero.ktorcito_compiler.util.getValue
import com.gucodero.ktorcito_compiler.util.withQuotes

class QueryInfo(
    parameter: KSValueParameter
) {
    private val key: String = parameter.getAnnotationOfType<Query>().first().getValue("key")
    private val parameterName: String = parameter.name!!.asString()

    override fun toString(): String {
        return ".query(${key.withQuotes()}, $parameterName)"
    }
}
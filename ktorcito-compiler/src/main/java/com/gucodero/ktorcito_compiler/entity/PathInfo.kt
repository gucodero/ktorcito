package com.gucodero.ktorcito_compiler.entity

import com.google.devtools.ksp.symbol.KSValueParameter
import com.gucodero.ktorcito.annotations.Path
import com.gucodero.ktorcito_compiler.util.getAnnotationOfType
import com.gucodero.ktorcito_compiler.util.getValue
import com.gucodero.ktorcito_compiler.util.withQuotes

class PathInfo(
    parameter: KSValueParameter
) {
    private val key: String = parameter.getAnnotationOfType<Path>().first().getValue("key")
    private val parameterName: String = parameter.name!!.asString()

    override fun toString(): String {
        return ".path(${key.withQuotes()}, $parameterName)"
    }
}
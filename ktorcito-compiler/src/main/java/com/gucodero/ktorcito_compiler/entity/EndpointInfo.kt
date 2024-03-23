package com.gucodero.ktorcito_compiler.entity

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.gucodero.ktorcito.Response
import com.gucodero.ktorcito.annotations.Body
import com.gucodero.ktorcito.annotations.Path
import com.gucodero.ktorcito.annotations.Query
import com.gucodero.ktorcito_compiler.util.getGenericType
import com.gucodero.ktorcito_compiler.util.getParametersWithAnnotation
import com.gucodero.ktorcito_compiler.util.isInstanceOf
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.typeNameOf

internal class EndpointInfo(
    val type: EndpointType,
    val declaration: KSFunctionDeclaration,
    val path: String
) {

    val httpMethod: String = type.funcName
    val functionName = declaration.simpleName.asString()
    val modifiers: List<KModifier> = declaration.modifiers.mapNotNull {
        it.toKModifier()
    }

    val parameters: List<ParameterSpec> = declaration.parameters.map {
        ParameterSpec.builder(
            name = it.name!!.asString(),
            type = it.type.toTypeName()
        ).build()
    }
    val parametersWithPathAnnotation = declaration.getParametersWithAnnotation<Path>()
        .map {
            PathInfo(it)
        }
    val parametersWithQueryAnnotation = declaration.getParametersWithAnnotation<Query>()
        .map {
            QueryInfo(it)
        }
    val hasBody: Boolean = declaration.getParametersWithAnnotation<Body>().isNotEmpty()
    val bodyParameter: BodyInfo = declaration.getParametersWithAnnotation<Body>()
        .firstOrNull()
        ?.name
        ?.asString()
        .orEmpty()
        .let {
            BodyInfo(it)
        }

    val returnTypeName: TypeName = declaration.returnType?.toTypeName() ?: typeNameOf<Unit>()
    val returnIsUnit: Boolean = declaration.returnType.isInstanceOf<Unit>()
    val returnIsResponse: Boolean = declaration.returnType.isInstanceOf<Response<*>>()
    val responseGenericTypeIsUnit: Boolean get() = declaration.returnType.getGenericType(0)
        .isInstanceOf<Unit>()
}
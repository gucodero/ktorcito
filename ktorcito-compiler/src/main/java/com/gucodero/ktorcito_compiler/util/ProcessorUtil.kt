package com.gucodero.ktorcito_compiler.util

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueParameter
import com.gucodero.ktorcito_compiler.entity.EndpointInfo
import com.gucodero.ktorcito_compiler.entity.EndpointType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.typeNameOf

internal fun KSClassDeclaration.getEndpointFunctions(): List<EndpointInfo> {
    return getAllFunctions().map { declaration ->
        val endpointType: EndpointType? = EndpointType.findByAnnotationName(
            declaration.annotations.lastOrNull()?.shortName?.asString().orEmpty()
        )
        endpointType?.let {
            EndpointInfo(
                type = endpointType,
                declaration = declaration,
                path = declaration.annotations.last().getValue("path")
            )
        }
    }.filterNotNull().toList()
}

internal fun FunSpec.Builder.simpleCodeBlock(
    suffix: String,
    builderAction: CodeBlock.Builder.() -> Unit
) {
    beginControlFlow("$suffix {")
    addCode(com.squareup.kotlinpoet.buildCodeBlock(builderAction))
    endControlFlow()
}

internal inline fun <reified T> KSAnnotation.getValue(key: String): T {
    return this.arguments.find { it.name?.asString() == key }?.value as T
}

@OptIn(KspExperimental::class)
internal inline fun <reified T: Annotation> KSFunctionDeclaration.getParametersWithAnnotation(): List<KSValueParameter> {
    return parameters.filter {
        it.isAnnotationPresent(T::class)
    }
}

internal inline fun <reified T: Annotation> KSValueParameter.getAnnotationOfType(): Sequence<KSAnnotation> {
    return annotations.filter {
        it.annotationType.resolve().toClassName().simpleName == T::class.simpleName
    }
}

internal inline fun <reified T> KSTypeReference?.isInstanceOf(): Boolean {
    return this?.resolve()?.toClassName()
        ?.canonicalName == T::class.asClassName().canonicalName
}

internal fun KSTypeReference?.getGenericType(index: Int): KSTypeReference? {
    return this?.resolve()?.arguments?.getOrNull(index)?.type
}

internal inline fun <reified T> propertyBuilder(name: String): PropertySpec.Builder {
    return PropertySpec.builder(
        name,
        typeNameOf<T>()
    )
}

internal fun String.withQuotes(): String = "\"$this\""
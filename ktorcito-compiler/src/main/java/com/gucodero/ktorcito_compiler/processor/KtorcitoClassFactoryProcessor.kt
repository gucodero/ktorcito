package com.gucodero.ktorcito_compiler.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.gucodero.ktorcito.KtorcitoFactory
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.typeNameOf
import io.ktor.client.HttpClient
import com.gucodero.ktorcito_compiler.util.*

class KtorcitoClassFactoryProcessor(
    private val clazz: KSClassDeclaration,
    private val logger: KSPLogger,
) {

    fun process(): TypeSpec {

        logger.warn(
            message = "Processing factory class for ${clazz.simpleName.asString()}"
        )
        //CREATE FUNCTION BUILDER
        val factoryFunBuilder: FunSpec.Builder = createFunBuilder()

        //ADD FUNCTION BODY
        factoryFunBuilder.createFunctionBody()

        //CREATE FACTORY CLASS
        return createClassBuilder()
            .addFunction(factoryFunBuilder.build())
            .build()
    }

    private fun createFunBuilder(): FunSpec.Builder {
        return FunSpec.builder("create")
            .addModifiers(KModifier.OVERRIDE)
            .returns(clazz.toClassName())
            .addParameter(
                name = BASE_URL_PROPERTY_NAME,
                type = typeNameOf<String>()
            )
            .addParameter(
                name = HTTP_CLIENT_PROPERTY_NAME,
                type = typeNameOf<HttpClient>()
            )
    }

    private fun FunSpec.Builder.createFunctionBody() {
        addStatement("val instance = ${clazz.toClassName().simpleName}$SUFFIX_IMPL_CLASS(")
        addStatement(" $BASE_URL_PROPERTY_NAME = $BASE_URL_PROPERTY_NAME,")
        addStatement(" $HTTP_CLIENT_PROPERTY_NAME = $HTTP_CLIENT_PROPERTY_NAME")
        addStatement(")")
        addStatement("return instance")
    }

    private fun createClassBuilder(): TypeSpec.Builder {
        return TypeSpec.objectBuilder(name = "${clazz.simpleName.asString()}$SUFFIX_FACTORY_CLASS")
            .addSuperinterface(
                KtorcitoFactory::class.asClassName().parameterizedBy(clazz.toClassName())
            )
    }
}
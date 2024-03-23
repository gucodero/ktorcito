package com.gucodero.ktorcito_compiler.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.gucodero.ktorcito_compiler.entity.EndpointInfo
import com.gucodero.ktorcito_compiler.util.getEndpointFunctions
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.typeNameOf
import io.ktor.client.HttpClient
import com.gucodero.ktorcito_compiler.util.*

internal class KtorcitoClassImplProcessor(
    private val clazz: KSClassDeclaration,
    private val logger: KSPLogger,
) {

    private val endpointsInfo: List<EndpointInfo> = clazz.getEndpointFunctions()
    val httpMethods = endpointsInfo.map { it.httpMethod }.distinct().toTypedArray()

    fun process(): TypeSpec {

        logger.warn(
            message = "Processing class ${clazz.simpleName.asString()}"
        )
        //CREATE CLASS BUILDER
        val classBuilder: TypeSpec.Builder = createClassBuilder()

        //CREATE CONSTRUCTOR
        val constructorFunSpec: FunSpec = createConstructor()
        classBuilder.primaryConstructor(constructorFunSpec)

        //CREATE PROPERTIES
        val propertiesSpec: List<PropertySpec> = createProperties()
        classBuilder.addProperties(propertiesSpec)

        logger.warn(
            message = "Init processing functions for class ${clazz.simpleName.asString()}"
        )
        //CREATE FUNCTIONS
        val endpointsImpl: List<FunSpec> = endpointsInfo.map {
            KtorcitoFunctionProcessor(
                clazz = clazz,
                logger = logger,
                endpointInfo = it
            ).process()
        }
        classBuilder.addFunctions(endpointsImpl)

        return classBuilder.build()
    }

    private fun createClassBuilder(): TypeSpec.Builder {
        val implClassName = "${clazz.simpleName.asString()}${SUFFIX_IMPL_CLASS}"
        return TypeSpec.classBuilder(implClassName)
            .addSuperinterface(clazz.toClassName())
            .addModifiers(KModifier.PRIVATE)
    }

    private fun createConstructor(): FunSpec {
        return FunSpec.constructorBuilder()
            .addParameter(
                name = BASE_URL_PROPERTY_NAME,
                type = typeNameOf<String>()
            )
            .addParameter(
                name = HTTP_CLIENT_PROPERTY_NAME,
                type = typeNameOf<HttpClient>()
            )
            .build()
    }

    private fun createProperties(): List<PropertySpec> {
        val baseUrlProperty: PropertySpec = propertyBuilder<String>(BASE_URL_PROPERTY_NAME)
            .initializer(BASE_URL_PROPERTY_NAME)
            .addModifiers(KModifier.PRIVATE)
            .build()
        val httpClientProperty: PropertySpec = propertyBuilder<HttpClient>(HTTP_CLIENT_PROPERTY_NAME)
            .initializer(HTTP_CLIENT_PROPERTY_NAME)
            .addModifiers(KModifier.PRIVATE)
            .build()
        return listOf(baseUrlProperty, httpClientProperty)
    }

}
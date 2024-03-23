package com.gucodero.ktorcito_compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.gucodero.ktorcito.annotations.Controller
import com.gucodero.ktorcito_compiler.processor.KtorcitoClassFactoryProcessor
import com.gucodero.ktorcito_compiler.processor.KtorcitoClassImplProcessor
import com.gucodero.ktorcito_compiler.util.SUFFIX_FILE_NAME
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo

class KtorcitoProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
): SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(
            annotationName = Controller::class.java.name
        ).filterIsInstance<KSClassDeclaration>().forEach(::processClass)
        return emptyList()
    }

    private fun processClass(
        clazz: KSClassDeclaration
    ) {

        logger.warn(
            message = "Init processing class ${clazz.simpleName.asString()}"
        )
        //PREPARE PROCESSORS
        val ktorcitoClassImplProcessor = KtorcitoClassImplProcessor(
            clazz = clazz,
            logger = logger
        )
        val ktorcitoClassFactoryProcessor = KtorcitoClassFactoryProcessor(
            clazz = clazz,
            logger = logger
        )

        //CREATE FILE BUILDER
        val fileSpecBuilder: FileSpec.Builder = FileSpec
            .builder(
                packageName = clazz.packageName.asString(),
                fileName = "${clazz.simpleName.asString()}$SUFFIX_FILE_NAME"
            )

        //ADD IMPORTS
        fileSpecBuilder.addImports(
            httpMethods = ktorcitoClassImplProcessor.httpMethods
        )

        //CREATE CLASS IMPLEMENTATION
        val classImpl: TypeSpec = ktorcitoClassImplProcessor.process()
        fileSpecBuilder.addType(classImpl)

        //CREATE FACTORY CLASS
        val factoryClass: TypeSpec = ktorcitoClassFactoryProcessor.process()
        fileSpecBuilder.addType(factoryClass)

        //WRITE TO FILE
        fileSpecBuilder.build().writeTo(
            codeGenerator,
            Dependencies(
                aggregating = false
            )
        )
    }

    private fun FileSpec.Builder.addImports(
        httpMethods: Array<String>
    ){
        listOf(
            "io.ktor.client.request" to httpMethods,
            "kotlinx.coroutines" to arrayOf("withContext", "Dispatchers"),
            "com.gucodero.ktorcito" to arrayOf("jsonBody", "setJsonBody", "KtorcitoURL"),
            "io.ktor.client.statement" to arrayOf("HttpResponse")
        ).forEach { (packageName: String, names: Array<String>) ->
            addImport(
                packageName = packageName,
                names = names
            )
        }
    }
}
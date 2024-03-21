package com.gucodero.ktorcito_compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.gucodero.ktorcito.annotations.HelloWorld
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import com.squareup.kotlinpoet.typeNameOf

class KtorcitoProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
): SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(
            annotationName = HelloWorld::class.java.name
        ).filterIsInstance<KSClassDeclaration>().forEach(::processClass)
        return emptyList()
    }

    private fun processClass(
        clazz: KSClassDeclaration
    ) {
        logger.warn("Processing @HelloWorld in class: ${clazz.simpleName.asString()}")
        val funSpec: FunSpec = FunSpec.builder("helloWorld")
            .addModifiers(KModifier.PUBLIC)
            .receiver(clazz.toClassName())
            .returns(typeNameOf<String>())
            .addStatement("return \"Hello, World!\"")
            .build()
        val fileSpec: FileSpec = FileSpec
            .builder(
                packageName = clazz.packageName.asString(),
                fileName = "HelloWorld_Generated"
            )
            .addFunction(funSpec)
            .build()
        fileSpec.writeTo(codeGenerator, Dependencies(false))
    }

}
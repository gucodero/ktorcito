package com.gucodero.ktorcito_compiler.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.gucodero.ktorcito_compiler.entity.EndpointInfo
import com.gucodero.ktorcito_compiler.util.simpleCodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.gucodero.ktorcito_compiler.util.*
import com.squareup.kotlinpoet.CodeBlock

internal class KtorcitoFunctionProcessor(
    private val clazz: KSClassDeclaration,
    private val logger: KSPLogger,
    private val endpointInfo: EndpointInfo
) {

    fun process(): FunSpec {
        logger.warn(
            message = "Processing function ${endpointInfo.functionName} for class ${clazz.simpleName.asString()}"
        )
        //CREATE FUNCTION BUILDER
        val funBuilder: FunSpec.Builder = createBuilder()

        //CREATE FUNCTION BODY
        funBuilder.simpleCodeBlock("return withContext(Dispatchers.IO)") {
            addUrlBuilderSection()
            addHttpRequestSection()
            addReturnSection()
        }

        return funBuilder.build()
    }

    private fun createBuilder(): FunSpec.Builder {
        return FunSpec.builder(endpointInfo.functionName)
            .addModifiers(KModifier.OVERRIDE)
            .addModifiers(endpointInfo.modifiers)
            .addParameters(endpointInfo.parameters)
            .returns(endpointInfo.returnTypeName)
    }

    private fun CodeBlock.Builder.addUrlBuilderSection() {
        addStatement("val ktorcitoURL: KtorcitoURL = KtorcitoURL.Builder()")
        addStatement("  .baseUrl($BASE_URL_PROPERTY_NAME)")
        addStatement("  .endpoint(${endpointInfo.path.withQuotes()})")
        endpointInfo.parametersWithPathAnnotation.forEach {
            addStatement("  $it")
        }
        endpointInfo.parametersWithQueryAnnotation.forEach {
            addStatement("  $it")
        }
        addStatement("  .build()")
    }

    private fun CodeBlock.Builder.addHttpRequestSection() {
        addStatement("val result: HttpResponse = ${HTTP_CLIENT_PROPERTY_NAME}.${endpointInfo.httpMethod}(")
        addStatement("  urlString = ktorcitoURL.url")
        if (endpointInfo.hasBody) {
            addStatement(") {")
            addStatement("  ${endpointInfo.bodyParameter}")
            addStatement("}")
        } else {
            addStatement(")")
        }
    }

    private fun CodeBlock.Builder.addReturnSection() {
        if (!endpointInfo.returnIsUnit) {
            if (endpointInfo.returnIsResponse) {
                addStatement("Response(")
                addStatement("  httpResponse = result,")
                if (endpointInfo.responseGenericTypeIsUnit) {
                    addStatement("  body = Unit")
                } else {
                    addStatement("  body = result.jsonBody()")
                }
                addStatement(")")
            } else {
                addStatement("result.jsonBody()")
            }
        }
    }
}
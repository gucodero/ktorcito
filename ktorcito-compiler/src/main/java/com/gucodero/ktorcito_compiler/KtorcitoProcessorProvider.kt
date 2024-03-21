package com.gucodero.ktorcito_compiler

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KtorcitoProcessorProvider: SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return KtorcitoProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }

}
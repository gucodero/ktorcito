package com.gucodero.ktorcito_app.common

import java.util.Locale

fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase())
        it.titlecase(Locale.getDefault())
    else
        it.toString()
}
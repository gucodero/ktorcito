package com.gucodero.ktorcito_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gucodero.ktorcito_app.ui.main_screen.MainScreen
import com.gucodero.ktorcito_app.ui.theme.KtorcitoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KtorcitoTheme {
                MainScreen()
            }
        }
    }
}
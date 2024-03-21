package com.gucodero.ktorcito_app.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingScaffold(
    isLoading: Boolean,
    topBar: @Composable ColumnScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable ConstraintLayoutScope.() -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                topBar()
            }
        },
        floatingActionButton = floatingActionButton
    ) {
        if (isLoading) {
            BasicAlertDialog(
                onDismissRequest = {}
            ) {
                Surface(
                    shape = RoundedCornerShape(
                        24.dp
                    ),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            content = content
        )
    }
}
package com.gucodero.ktorcito_app.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@Composable
fun BaseViewModel<*, *>.isLoading(): State<Boolean> {
    return loadingFlow.collectAsState()
}

@Composable
fun <UiState> BaseViewModel<UiState, *>.uiState(): State<UiState> {
    return uiStateFlow.collectAsState()
}

fun <UiState> BaseViewModel<UiState, *>.setState(block: UiState.() -> UiState) {
    val newState: UiState = uiState.block()
    setState(newState)
}

@Composable
fun <UiEvent> OnEvent(
    channel: Channel<UiEvent>,
    block: suspend CoroutineScope.(UiEvent) -> Unit
) {
    val eventFlow: Flow<UiEvent> = remember {
        channel.consumeAsFlow()
    }
    LaunchedEffect(Unit) {
        eventFlow.collect {
            block(it)
        }
    }
}

fun ViewModel.launch(
    context: CoroutineContext = Dispatchers.Main,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return viewModelScope.launch(
        context = context,
        start = start,
        block = block
    )
}
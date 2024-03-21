package com.gucodero.ktorcito_app.ui.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<UiState, UiEvent>(
    defaultState: () -> UiState
): ViewModel() {

    val loadingFlow = MutableStateFlow(false)
    val uiStateFlow = MutableStateFlow(defaultState())
    val uiEventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val uiState: UiState get() = uiStateFlow.value

    protected var isLoading: Boolean
        get() = loadingFlow.value
        set(value) {
            loadingFlow.value = value
        }

    fun setState(state: UiState) {
        uiStateFlow.value = state
    }

    fun UiEvent.send(){
        uiEventChannel.trySend(this)
    }

}
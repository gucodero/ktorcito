package com.gucodero.ktorcito_app.ui.main_screen

import com.gucodero.ktorcito_app.domain.post.entity.Post

sealed class MainUiEvent {
    data class ShowPostDetails(
        val post: Post
    ): MainUiEvent()
    data object ClosePostEdit: MainUiEvent()
    data object ClosePostCreate: MainUiEvent()
}
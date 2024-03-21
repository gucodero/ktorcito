package com.gucodero.ktorcito_app.ui.main_screen

import com.gucodero.ktorcito_app.domain.post.entity.Post

data class MainUiState(
    val posts: List<Post> = emptyList(),
    val users: Set<Int> = emptySet()
)
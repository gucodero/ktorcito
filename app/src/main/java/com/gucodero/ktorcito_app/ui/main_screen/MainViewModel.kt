package com.gucodero.ktorcito_app.ui.main_screen

import com.gucodero.ktorcito.Response
import com.gucodero.ktorcito_app.data.post.PostRepository
import com.gucodero.ktorcito_app.domain.post.entity.Post
import com.gucodero.ktorcito_app.ui.common.BaseViewModel
import com.gucodero.ktorcito_app.ui.common.launch
import com.gucodero.ktorcito_app.ui.common.setState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PostRepository
): BaseViewModel<MainUiState, MainUiEvent>(
    defaultState = {
        MainUiState()
    }
) {

    init {
        getPosts()
    }

    fun getPosts(
        userId: Int? = null
    ) = launch {
        isLoading = true
        val result: Response<List<Post>> = repository.getPosts(
            userId = userId
        )
        isLoading = false
        if (result.isSuccessful) {
            setState {
                copy(
                    posts = result.data,
                    users = if (userId == null) {
                        result.data.map { it.userId }.toSet()
                    } else {
                        users
                    }
                )
            }
        }
    }

    fun getPostById(id: Int) = launch {
        isLoading = true
        val result: Response<Post?> = repository.getPostById(id)
        isLoading = false
        result.data?.let { post ->
            MainUiEvent.ShowPostDetails(
                post = post
            ).send()
        }
    }

    fun createPost(
        userId: Int,
        title: String,
        body: String
    ) = launch {
        isLoading = true
        val result: Response<Post> = repository.createPost(
            userId = userId,
            title = title,
            body = body
        )
        isLoading = false
        setState {
            copy(
                posts = listOf(result.data) + posts
            )
        }
        MainUiEvent.ClosePostCreate.send()
    }

    fun updatePost(
        id: Int,
        userId: Int,
        title: String,
        body: String,
        withPatchEndpoint: Boolean = false
    ) = launch {
        isLoading = true
        val result: Response<Post> = if (withPatchEndpoint) {
            repository.patchPost(
                id = id,
                userId = userId,
                title = title,
                body = body
            )
        } else {
            repository.updatePost(
                id = id,
                userId = userId,
                title = title,
                body = body
            )
        }
        isLoading = false
        setState {
            copy(
                posts = posts.map {
                    if (it.id == id) {
                        result.data
                    } else {
                        it
                    }
                }
            )
        }
        MainUiEvent.ClosePostEdit.send()
    }

    fun deletePost(id: Int) = launch {
        isLoading = true
        repository.deletePost(id)
        isLoading = false
        setState {
            copy(
                posts = posts.filter {
                    it.id != id
                }
            )
        }
    }

}
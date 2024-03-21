package com.gucodero.ktorcito_app.data.post

import com.gucodero.ktorcito_app.data.common.util.Response
import com.gucodero.ktorcito_app.data.common.util.map
import com.gucodero.ktorcito_app.data.common.util.transform
import com.gucodero.ktorcito_app.data.post.dto.request.PostPatchRequest
import com.gucodero.ktorcito_app.data.post.dto.request.PostRequest
import com.gucodero.ktorcito_app.data.post.mapper.toDomain
import com.gucodero.ktorcito_app.data.post.remote.PostApi
import com.gucodero.ktorcito_app.domain.post.entity.Post
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: PostApi
): PostRepository {

    override suspend fun getPosts(
        userId: Int?
    ): Response<List<Post>> {
        return api.getPosts(
            userId = userId
        ).map {
            it.toDomain()
        }
    }

    override suspend fun getPostById(id: Int): Response<Post?> {
        return api.getPostById(id).transform {
            it?.toDomain()
        }
    }

    override suspend fun createPost(
        userId: Int,
        title: String,
        body: String
    ): Response<Post> {
        return api.createPost(
            PostRequest(
                userId = userId,
                title = title,
                body = body
            )
        ).transform {
            Post(
                id = it.id,
                userId = userId,
                title = title,
                body = body
            )
        }
    }

    override suspend fun updatePost(
        id: Int,
        userId: Int,
        title: String,
        body: String
    ): Response<Post> {
        return api.updatePost(
            id = id,
            placeholder = PostRequest(
                userId = userId,
                title = title,
                body = body
            )
        ).transform {
            it.toDomain()
        }
    }

    override suspend fun patchPost(
        id: Int,
        userId: Int?,
        title: String?,
        body: String?
    ): Response<Post> {
        return api.patchPost(
            id = id,
            placeholder = PostPatchRequest(
                userId = userId,
                title = title,
                body = body
            )
        ).transform {
            it.toDomain()
        }
    }

    override suspend fun deletePost(id: Int): Response<Unit> {
        return api.deletePost(id)
    }

}
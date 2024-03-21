package com.gucodero.ktorcito_app.data.common.di

import com.gucodero.ktorcito_app.data.post.PostRepository
import com.gucodero.ktorcito_app.data.post.PostRepositoryImpl
import com.gucodero.ktorcito_app.data.post.remote.PostApi
import com.gucodero.ktorcito_app.data.post.remote.PostApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface DataBindModule {

    @Binds
    fun bindPlaceholderApi(placeholderApi: PostApiImpl): PostApi

    @Binds
    fun bindPlaceholderRepository(
        placeholderRepository: PostRepositoryImpl
    ): PostRepository

}
package com.gucodero.ktorcito_app.data.common.di

import com.gucodero.ktorcito_app.data.post.PostRepository
import com.gucodero.ktorcito_app.data.post.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface DataBindModule {

    @Binds
    fun bindPlaceholderRepository(
        placeholderRepository: PostRepositoryImpl
    ): PostRepository

}
package com.gucodero.ktorcito_app.data.common.di

import android.util.Log
import com.gucodero.ktorcito.Ktorcito
import com.gucodero.ktorcito_app.data.post.remote.PostApi
import com.gucodero.ktorcito_app.data.post.remote.PostApiFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.i("KtorHttpLogger", message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    @Provides
    @Singleton
    fun provideKtorcito(
        httpClient: HttpClient
    ): Ktorcito {
        return Ktorcito.Builder()
            .httpClient(httpClient)
            .baseUrl("https://jsonplaceholder.typicode.com")
            .build()
    }

    @Provides
    fun providePostApi(
        ktorcito: Ktorcito
    ): PostApi {
        return ktorcito.create(PostApiFactory)
    }

}
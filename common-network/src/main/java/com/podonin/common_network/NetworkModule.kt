package com.podonin.common_network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://hr-challenge.dev.tapyou.com/")
            .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
            .build()
    }

    private val json = Json { ignoreUnknownKeys = true }
}

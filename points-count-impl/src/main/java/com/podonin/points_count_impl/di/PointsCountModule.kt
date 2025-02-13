package com.podonin.points_count_impl.di

import com.podonin.points_count_api.navigation.PointsCountScreenProvider
import com.podonin.points_count_impl.data.ApiService
import com.podonin.points_count_impl.data.PointsCountRepositoryImpl
import com.podonin.points_count_impl.domain.PointsCountInteractor
import com.podonin.points_count_impl.domain.PointsCountInteractorImpl
import com.podonin.points_count_impl.domain.repository.PointsCountRepository
import com.podonin.points_count_impl.navigation.PointsCountScreenProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PointsCountModule {
    @Binds
    @Singleton
    abstract fun getPointsCountCoordinator(impl: PointsCountScreenProviderImpl): PointsCountScreenProvider

    @Binds
    @Singleton
    abstract fun getPointsCountRepository(impl: PointsCountRepositoryImpl): PointsCountRepository

    @Binds
    abstract fun getPointsCountInteractor(impl: PointsCountInteractorImpl): PointsCountInteractor

    companion object {
        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}
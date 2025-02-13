package com.podonin.files_saver_impl.di

import com.podonin.files_saver_api.domain.PhotoSaverInteractor
import com.podonin.files_saver_impl.data.FileRepositoryImpl
import com.podonin.files_saver_impl.data.MediaStorageRepositoryImpl
import com.podonin.files_saver_impl.domain.repository.FileRepository
import com.podonin.files_saver_impl.domain.repository.MediaStorageRepository
import com.podonin.files_saver_impl.domain.PhotoSaverInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PhotoSaverModule {
    @Binds
    @Singleton
    abstract fun bindFileRepository(impl: FileRepositoryImpl): FileRepository

    @Binds
    @Singleton
    abstract fun bindMediaStorageRepository(impl: MediaStorageRepositoryImpl): MediaStorageRepository

    @Binds
    abstract fun bindPhotoSaverInteractor(impl: PhotoSaverInteractorImpl): PhotoSaverInteractor
}
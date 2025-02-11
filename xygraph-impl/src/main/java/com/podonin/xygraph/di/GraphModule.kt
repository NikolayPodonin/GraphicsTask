package com.podonin.xygraph.di

import com.podonin.xygraph.navigation.GraphScreenProviderImpl
import com.podonin.xygraph_api.navigation.GraphScreenProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GraphModule {
    @Binds
    @Singleton
    abstract fun bindXYGraphCoordinator(impl: GraphScreenProviderImpl): GraphScreenProvider
}
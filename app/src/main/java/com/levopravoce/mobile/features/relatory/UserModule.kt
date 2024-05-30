package com.levopravoce.mobile.features.relatory

import com.levopravoce.mobile.config.ApiClient
import com.levopravoce.mobile.features.relatory.data.RelatoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RelatoryModule {

    @Provides
    @Singleton
    fun provideRelatoryRepository(
        apiClient: ApiClient
    ): RelatoryRepository {
        return apiClient.buildApi(RelatoryRepository::class.java)
    }
}
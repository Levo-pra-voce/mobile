package com.levopravoce.mobile.features.newOrder

import com.levopravoce.mobile.config.ApiClient
import com.levopravoce.mobile.features.newOrder.data.NewOrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NewOrderModule {
    @Provides
    @Singleton
    fun provideNewOrderRepository(
        apiClient: ApiClient
    ): NewOrderRepository {
        return apiClient.buildApi(NewOrderRepository::class.java)
    }
}
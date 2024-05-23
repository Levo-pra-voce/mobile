package com.levopravoce.mobile.features.order;

import com.levopravoce.mobile.config.ApiClient
import com.levopravoce.mobile.features.order.data.OrderRepository
import dagger.Module;
import dagger.Provides
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OrderModule {

    @Provides
    @Singleton
    fun provideOrderRepository(
        apiClient: ApiClient
    ): OrderRepository {
        return apiClient.buildApi(OrderRepository::class.java)
    }
}
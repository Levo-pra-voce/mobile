package com.levopravoce.mobile.features.user

import com.levopravoce.mobile.config.ApiClient
import com.levopravoce.mobile.features.user.data.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        apiClient: ApiClient
    ): UserRepository {
        return apiClient.buildApi(UserRepository::class.java)
    }
}
package com.levopravoce.mobile.features.auth;

import com.levopravoce.mobile.config.ApiClient
import com.levopravoce.mobile.features.auth.data.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AuthModule {
    @Provides
    fun provideAuthRepository(
        apiClient: ApiClient
    ): AuthRepository {
        return apiClient.buildApi(AuthRepository::class.java)
    }
}

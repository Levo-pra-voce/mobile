package com.levopravoce.mobile.features.auth;

import android.content.Context
import com.levopravoce.mobile.config.ApiClient
import com.levopravoce.mobile.features.auth.data.AuthRepository
import com.levopravoce.mobile.features.auth.domain.AuthStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object AuthModule {
    @Provides
    fun provideAuthRepository(
        apiClient: ApiClient
    ): AuthRepository {
        return apiClient.buildApi(AuthRepository::class.java)
    }

    @Provides
    fun provideAuthStore(
        @ApplicationContext context: Context
    ): AuthStore {
        return AuthStore(context)
    }
}

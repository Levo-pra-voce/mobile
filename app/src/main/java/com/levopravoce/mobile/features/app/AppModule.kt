package com.levopravoce.mobile.features.app

import android.content.Context
import com.levopravoce.mobile.config.PreferencesManager
import com.levopravoce.mobile.features.auth.domain.AuthStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAuthStore(
        @ApplicationContext appContext: Context
    ): AuthStore {
        return AuthStore(appContext)
    }

    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext appContext: Context
    ): PreferencesManager {
        return PreferencesManager(appContext)
    }
}
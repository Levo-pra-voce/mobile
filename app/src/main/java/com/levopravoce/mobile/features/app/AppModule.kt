package com.levopravoce.mobile.features.app

import android.content.Context
import androidx.room.Room
import com.levopravoce.mobile.config.PreferencesManager
import com.levopravoce.mobile.database.LevoPraVoceDatabase
import com.levopravoce.mobile.features.auth.domain.AuthStore
import com.levopravoce.mobile.features.chat.data.MessageDao
import com.levopravoce.mobile.features.chat.data.MessageDatabaseRepository
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

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): LevoPraVoceDatabase {
        return Room.databaseBuilder(
            appContext,
            LevoPraVoceDatabase::class.java,
            LevoPraVoceDatabase.DATABASE_NAME
        ).build();
    }

    @Provides
    @Singleton
    fun provideMessageDao(
        database: LevoPraVoceDatabase
    ) = database.messageDao()

    @Provides
    @Singleton
    fun provideMessageDatabaseRepository(
        messageDao: MessageDao,
        @ApplicationContext context: Context
    ) = MessageDatabaseRepository(messageDao, context)
}
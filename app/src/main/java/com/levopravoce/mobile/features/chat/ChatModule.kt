package com.levopravoce.mobile.features.chat

import com.levopravoce.mobile.config.ApiClient
import com.levopravoce.mobile.features.chat.data.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChatModule {
    @Provides
    @Singleton
    fun provideChatRepository(
        apiClient: ApiClient
    ): ChatRepository {
        return apiClient.buildApi(ChatRepository::class.java)
    }
}
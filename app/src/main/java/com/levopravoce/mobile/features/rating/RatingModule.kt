package com.levopravoce.mobile.features.rating

import com.levopravoce.mobile.config.ApiClient
import com.levopravoce.mobile.features.rating.data.RatingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RatingModule {
    @Provides
    @Singleton
    fun provideRatingRepository(
        apiClient: ApiClient
    ): RatingRepository {
        return apiClient.buildApi(RatingRepository::class.java)
    }
}
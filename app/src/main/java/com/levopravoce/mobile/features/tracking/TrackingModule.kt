package com.levopravoce.mobile.features.tracking

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.levopravoce.mobile.features.tracking.domain.ILocationService
import com.levopravoce.mobile.features.tracking.domain.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackingModule {

    @Singleton
    @Provides
    fun provideLocationClient(
        @ApplicationContext context: Context
    ): ILocationService = LocationService(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )
}
package com.example.weather.di

import com.example.weather.domain.services.DefaultLocationService
import com.example.weather.domain.services.LocationService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Binds
    @Singleton
    abstract fun bindLocationTracker(defaultLocationTracker: DefaultLocationService) : LocationService
}
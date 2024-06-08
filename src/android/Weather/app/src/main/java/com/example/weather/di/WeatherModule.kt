package com.example.weather.di

import com.example.weather.data.repository.WeatherServiceImpl
import com.example.weather.domain.services.WeatherService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(weatherServiceImpl: WeatherServiceImpl) : WeatherService
}
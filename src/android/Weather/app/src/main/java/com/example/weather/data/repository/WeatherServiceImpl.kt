package com.example.weather.data.repository

import com.example.weather.data.api.WeatherApiClient
import com.example.weather.domain.mappers.toWeatherInfo
import com.example.weather.domain.models.WeatherInfo
import com.example.weather.domain.services.WeatherService
import com.example.weather.domain.utils.ResourceEither
import javax.inject.Inject

class WeatherServiceImpl
    @Inject
    constructor(
        private val weatherApi: WeatherApiClient)
    : WeatherService {
    override suspend fun getWeatherData(lat: Double, long: Double): ResourceEither<WeatherInfo> {
        return try {
            ResourceEither.Success(
                data = weatherApi.getWeatherData(
                lat = lat,
                long = long
                ).toWeatherInfo()
            )
        } catch (e : java.lang.Exception){
            e.printStackTrace()
            ResourceEither.Error(e.message ?: "An unknown error occurred.")
        }
    }
}
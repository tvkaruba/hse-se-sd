package com.example.weather.domain.services

import com.example.weather.domain.models.WeatherInfo
import com.example.weather.domain.utils.ResourceEither

interface WeatherService {

    suspend fun getWeatherData(lat: Double, long : Double) : ResourceEither<WeatherInfo>
}
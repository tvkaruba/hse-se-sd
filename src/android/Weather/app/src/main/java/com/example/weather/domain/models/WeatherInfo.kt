package com.example.weather.domain.models

data class WeatherInfo(
    val weatherDataPerDay : Map<Int, List<WeatherData>>,
    val currentWeatherData : WeatherData?
)

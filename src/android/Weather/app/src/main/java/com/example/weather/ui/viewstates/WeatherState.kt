package com.example.weather.ui.viewstates

import com.example.weather.domain.models.WeatherInfo

data class WeatherState(
    val weatherInfo : WeatherInfo? = null,
    val isLoading : Boolean = false,
    val error: String? = null)
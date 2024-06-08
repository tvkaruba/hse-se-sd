package com.example.weather.ui.intents

sealed class WeatherIntent {

    data object FetchForecast : WeatherIntent()
}
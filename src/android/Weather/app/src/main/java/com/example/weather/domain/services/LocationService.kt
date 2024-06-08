package com.example.weather.domain.services

import android.location.Location

interface LocationService {

    suspend fun getCurrentLocation(): Location?
}
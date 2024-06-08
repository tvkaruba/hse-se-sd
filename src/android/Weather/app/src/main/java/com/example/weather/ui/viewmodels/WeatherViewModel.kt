package com.example.weather.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.services.LocationService
import com.example.weather.domain.services.WeatherService
import com.example.weather.domain.utils.ResourceEither
import com.example.weather.ui.viewstates.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
    @Inject
    constructor(
        private val weatherService: WeatherService,
        private val locationService: LocationService)
    : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    fun loadWeatherInfo(){
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )

            locationService.getCurrentLocation()?.let { location ->
                when (val result = weatherService.getWeatherData(location.latitude, location.longitude)){
                    is ResourceEither.Success -> {
                        state = state.copy(
                            isLoading = false,
                            error = null,
                            weatherInfo = result.data
                        )
                    }
                    is ResourceEither.Error -> {
                        state = state.copy(
                            isLoading = false,
                            error = result.message,
                            weatherInfo = null
                        )
                    }
                }
            } ?: kotlin.run {
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location."
                )
            }
        }
    }
}
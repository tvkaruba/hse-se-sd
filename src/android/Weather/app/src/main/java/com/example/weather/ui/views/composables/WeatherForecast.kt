package com.example.weather.ui.views.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.ui.viewstates.WeatherState

@Composable
fun WeatherForecast(
    state : WeatherState,
    modifier: Modifier = Modifier) {

    state.weatherInfo?.weatherDataPerDay?.get(0)?.let { data ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)) {
            Text(
                text = "Today",
                fontSize = 20.sp,
                color = Color.White)
            Spacer(
                modifier = Modifier.height(16.dp))
            LazyRow(
                content = {
                    items(data) {weatherData ->
                        HourlyWeatherRow(
                            weatherData = weatherData,
                            modifier = modifier
                                .height(100.dp)
                                .padding(horizontal = 16.dp))
                    }
                })
        }
    }
}
package org.example.domain.repository

import domain.models.WeatherData
import org.example.presentaion.utils.Resource

interface ApiRepository {
    suspend fun getWeather(city: String):Resource <WeatherData>
}
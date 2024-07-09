package domain.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    val city: String,
    val description: String,
    val humidity: Int,
    val temperature: Int,
    val windSpeed: Double,
)
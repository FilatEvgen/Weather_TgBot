package org.example.presentaion.controllers

import domain.models.WeatherData
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.MessageUpdate
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.domain.repository.ApiRepository
import org.example.domain.repository.ApiRepositoryImpl
import org.example.presentaion.controllers.ContollerRoutes.MESSAGING_ROUTE
import org.example.presentaion.utils.Resource

class MessagingController {
    private val _client = HttpClient(CIO) {
        install(Logging) {
            logger = io.ktor.client.plugins.logging.Logger.DEFAULT
            level = io.ktor.client.plugins.logging.LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    private val _repository: ApiRepository = ApiRepositoryImpl(_client)

    @InputHandler([MESSAGING_ROUTE])
    suspend fun messaging(update: MessageUpdate?, user: User, bot: TelegramBot) {
        val city = update?.text ?: "Москва"
        val weatherData = _repository.getWeather(city)
        handleWeatherResponse(weatherData, user, bot)
    }

    private suspend fun handleWeatherResponse(weatherData: Resource<WeatherData>, user: User, bot: TelegramBot) {
        when (weatherData) {
            is Resource.Success -> {
                val weather = weatherData.data!!
                val message = buildWeatherMessage(weather)
                message(message).send(user, bot)
                sendAdditionalMessage(user, bot)
            }

            is Resource.Error -> sendErrorMessage(user, bot, weatherData.message)
        }
        bot.inputListener[user] = MESSAGING_ROUTE
    }

    private fun buildWeatherMessage(weather: WeatherData): String {

        val conditions = weather.description.replaceFirstChar { it.uppercase() }
        val temperature = "${weather.temperature}°C"
        val humidity = "Влажность ${weather.humidity}%"
        val windSpeed = "Ветер ${weather.windSpeed} м/с"
        return "Погода в городе ${weather.city}: $conditions $temperature. $humidity$windSpeed"
    }

    private suspend fun sendErrorMessage(user: User, bot: TelegramBot, message: String?) {
        message(message ?: "Что то пошло не так...").send(user, bot)
    }

    private suspend fun sendAdditionalMessage(user: User, bot: TelegramBot) {
        message("Если вы хотите узнать погоду в другом городе, просто отправьте его название.").send(user, bot)
    }
}



package org.example.presentaion.controllers

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import org.example.presentaion.controllers.Commands.START
import org.example.presentaion.controllers.ContollerRoutes.MESSAGING_ROUTE

class StartController {
    @CommandHandler([START])
    suspend fun start(user: User, bot: TelegramBot) {
        val message = "Приветствуем вас в нашем погодном боте! Напишите название города, для которого хотите узнать погоду (например, «Москва» или «Владивосток») и я постараюсь его найти. Буду рад, если воспользуетесь моими услугами по поиску погоды ещё раз!"
        message { message }.send(user, bot)
        bot.inputListener[user] = MESSAGING_ROUTE
    }
}
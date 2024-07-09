package org.example

import eu.vendeli.tgbot.TelegramBot

suspend fun main() {
    val token = System.getenv("TG_TOKEN")
    val bot = TelegramBot(token)
    bot.handleUpdates()
}

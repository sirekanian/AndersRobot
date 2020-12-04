package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.api.WeatherApi
import com.sirekanyan.andersrobot.config.Config
import com.sirekanyan.andersrobot.config.ConfigKey.BOT_TOKEN
import com.sirekanyan.andersrobot.config.ConfigKey.BOT_USERNAME
import com.sirekanyan.andersrobot.extensions.getCityCommand
import com.sirekanyan.andersrobot.extensions.isCelsiusCommand
import com.sirekanyan.andersrobot.extensions.isWeatherCommand
import com.sirekanyan.andersrobot.extensions.sendText
import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.util.WebhookUtils

val botName = Config[BOT_USERNAME]

class AndersRobot : DefaultAbsSender(DefaultBotOptions()), LongPollingBot {

    private val weather = WeatherApi()

    override fun getBotUsername(): String = botName

    override fun getBotToken(): String = Config[BOT_TOKEN]

    override fun onUpdateReceived(update: Update) {
        val message = update.message
        val chatId = message.chatId
        println("${message.from?.id} => ${message.text}")
        val isBetterAccuracy = message.chatId == 314085103L || message.chatId == 106547051L
        val accuracy = if (isBetterAccuracy) 1 else 0
        val cityCommand = getCityCommand(message.text)
        when {
            !cityCommand.isNullOrEmpty() -> {
                val temperature = weather.getTemperature(cityCommand, accuracy)
                if (temperature == null) {
                    sendText(chatId, "Не знаю такого города")
                } else {
                    sendText(chatId, listOf(temperature).joinToString("\n"))
                }
            }
            isCelsiusCommand(message.text) -> {
                sendText(chatId, "Можешь звать меня просто Андерс")
            }
            isWeatherCommand(message.text) -> {
                val temperatures = weather.getTemperatures(accuracy)
                if (temperatures.isNotEmpty()) {
                    sendText(chatId, temperatures.joinToString("\n"))
                }
            }
        }
    }

    override fun clearWebhook() {
        WebhookUtils.clearWebhook(this)
    }

}
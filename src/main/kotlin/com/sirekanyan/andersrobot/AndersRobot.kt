package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.api.WeatherApi
import com.sirekanyan.andersrobot.config.Config
import com.sirekanyan.andersrobot.config.ConfigKey.BOT_TOKEN
import com.sirekanyan.andersrobot.config.ConfigKey.BOT_USERNAME
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
        when {
            isWeatherCommand(message.text) -> {
                val temperatures = weather.getTemperatures()
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
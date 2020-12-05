package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.api.WeatherApi
import com.sirekanyan.andersrobot.config.Config
import com.sirekanyan.andersrobot.config.ConfigKey.*
import com.sirekanyan.andersrobot.extensions.*
import com.sirekanyan.andersrobot.repository.CityRepositoryImpl
import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.util.WebhookUtils

val botName = Config[BOT_USERNAME]

class AndersRobot : DefaultAbsSender(DefaultBotOptions()), LongPollingBot {

    private val weather = WeatherApi()
    private val repository = CityRepositoryImpl(Config[DB_URL])

    override fun getBotUsername(): String = botName

    override fun getBotToken(): String = Config[BOT_TOKEN]

    override fun onUpdateReceived(update: Update) {
        val message = update.message
        val chatId = message.chatId
        println("${message.from?.id} (chat $chatId) => ${message.text}")
        val isBetterAccuracy = message.chatId == 314085103L || message.chatId == 106547051L
        val accuracy = if (isBetterAccuracy) 1 else 0
        val cityCommand = getCityCommand(message.text)
        val addCityCommand = getAddCityCommand(message.text)
        val delCityCommand = getDelCityCommand(message.text)
        when {
            !cityCommand.isNullOrEmpty() -> {
                val temperature = weather.getTemperature(cityCommand, accuracy)
                if (temperature == null) {
                    sendText(chatId, "Не знаю такого города")
                } else {
                    sendText(chatId, listOf(temperature).joinToString("\n"))
                }
            }
            !addCityCommand.isNullOrEmpty() -> {
                val temperature = weather.getTemperature(addCityCommand, accuracy)
                if (temperature == null) {
                    sendText(chatId, "Не знаю такого города")
                } else {
                    repository.putCity(chatId, addCityCommand)
                    showWeather(chatId, accuracy)
                }
            }
            !delCityCommand.isNullOrEmpty() -> {
                if (repository.deleteCity(chatId, delCityCommand)) {
                    sendText(chatId, "Удалено")
                } else {
                    sendText(chatId, "Нет такого города")
                }
            }
            isCelsiusCommand(message.text) -> {
                sendText(chatId, "Можешь звать меня просто Андерс")
            }
            isWeatherCommand(message.text) -> {
                showWeather(chatId, accuracy)
            }
        }
    }

    private fun showWeather(chatId: Long, accuracy: Int) {
        val dbCities = repository.getCities(chatId)
        val cities = if (dbCities.isEmpty()) listOf("Moscow") else dbCities
        val temperatures = weather.getTemperatures(cities, accuracy)
        if (temperatures.isNotEmpty()) {
            sendText(chatId, temperatures.joinToString("\n"))
        }
    }

    override fun clearWebhook() {
        WebhookUtils.clearWebhook(this)
    }

}
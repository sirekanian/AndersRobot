package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.api.WeatherApi
import com.sirekanyan.andersrobot.config.Config
import com.sirekanyan.andersrobot.config.ConfigKey.*
import com.sirekanyan.andersrobot.extensions.*
import com.sirekanyan.andersrobot.repository.CityRepositoryImpl
import com.sirekanyan.andersrobot.repository.supportedLanguages
import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.util.WebhookUtils

private const val DEFAULT_CITY_ID = 524901L // Moscow

val botName = Config[BOT_USERNAME]
val adminId = Config[ADMIN_ID].toLong()

class AndersRobot : DefaultAbsSender(DefaultBotOptions()), LongPollingBot {

    private val api = WeatherApi()
    private val repository = CityRepositoryImpl(Config[DB_URL])

    override fun getBotUsername(): String = botName

    override fun getBotToken(): String = Config[BOT_TOKEN]

    override fun onUpdateReceived(update: Update) {
        try {
            onUpdate(update)
        } catch (exception: Exception) {
            logError("Cannot handle update", exception)
            logError(update)
        }
    }

    private fun onUpdate(update: Update) {
        val message = update.message
        val chatId = message.chatId
        val language = message.from?.languageCode?.takeIf { it in supportedLanguages }
        println("${message.from?.id} (chat $chatId) => ${message.text}")
        val isBetterAccuracy = message.chatId == 314085103L || message.chatId == adminId
        val accuracy = if (isBetterAccuracy) 1 else 0
        val cityCommand = getCityCommand(message.text)
        val addCityCommand = getAddCityCommand(message.text)
        val delCityCommand = getDelCityCommand(message.text)
        when {
            message.hasLocation() -> {
                val weather = api.getWeather(message.location, language)
                if (weather == null) {
                    sendText(chatId, "Не знаю такого места")
                } else {
                    sendWeather(chatId, weather, accuracy)
                }
            }
            !cityCommand.isNullOrEmpty() -> {
                val weather = api.getWeather(cityCommand, language)
                if (weather == null) {
                    sendText(chatId, "Не знаю такого города")
                } else {
                    sendWeather(chatId, weather, accuracy)
                }
            }
            !addCityCommand.isNullOrEmpty() -> {
                val weather = api.getWeather(addCityCommand, language)
                if (weather == null) {
                    sendText(chatId, "Не знаю такого города")
                } else {
                    repository.putCity(chatId, weather.id)
                    showWeather(chatId, accuracy, language)
                }
            }
            !delCityCommand.isNullOrEmpty() -> {
                val temperature = api.getWeather(delCityCommand, language)
                when {
                    temperature == null -> sendText(chatId, "Не знаю такого города")
                    repository.deleteCity(chatId, temperature.id) -> sendText(chatId, "Удалено")
                    else -> sendText(chatId, "Нет такого города")
                }
            }
            isCelsiusCommand(message.text) -> {
                sendText(chatId, "Можешь звать меня просто Андерс")
            }
            isWeatherCommand(message.text) -> {
                showWeather(chatId, accuracy, language)
            }
        }
    }

    private fun showWeather(chatId: Long, accuracy: Int, language: String?) {
        val dbCities = repository.getCities(chatId)
        val cities = if (dbCities.isEmpty()) listOf(DEFAULT_CITY_ID) else dbCities
        val temperatures = api.getWeathers(cities, language)
        check(temperatures.isNotEmpty())
        sendText(chatId, temperatures.joinToString("\n") { it.format(accuracy) })
    }

    override fun clearWebhook() {
        logInfo("Cleared.")
        WebhookUtils.clearWebhook(this)
    }

    override fun onClosing() {
        logInfo("Closed.")
    }

}
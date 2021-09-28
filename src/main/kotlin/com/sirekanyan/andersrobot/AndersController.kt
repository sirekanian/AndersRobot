package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.api.Forecast
import com.sirekanyan.andersrobot.api.WeatherApi
import com.sirekanyan.andersrobot.extensions.sendPhoto
import com.sirekanyan.andersrobot.extensions.sendText
import com.sirekanyan.andersrobot.extensions.sendWeather
import com.sirekanyan.andersrobot.repository.CityRepository
import com.sirekanyan.andersrobot.repository.supportedLanguages
import jetbrains.letsPlot.export.ggsave
import org.telegram.telegrambots.meta.api.objects.Location
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import java.io.File
import java.util.*

private const val DEFAULT_CITY_ID = 524901L // Moscow

class AndersController(
    private val api: WeatherApi,
    private val repository: CityRepository,
    private val sender: AbsSender,
    update: Update,
) {

    private val chatId = update.message.chatId
    private val languageCode = update.message.from?.languageCode
    private val language = languageCode?.takeIf { it in supportedLanguages }
    private val locale = Locale.forLanguageTag(languageCode.orEmpty())

    fun onLocationCommand(location: Location) {
        val weather = api.getWeather(location, language)
        if (weather == null) {
            sender.sendText(chatId, "Не знаю такого места")
        } else {
            sender.sendWeather(chatId, weather)
        }
    }

    fun onCityCommand(city: String) {
        val weather = api.getWeather(city, language)
        if (weather == null) {
            sender.sendText(chatId, "Не знаю такого города")
        } else {
            sender.sendWeather(chatId, weather)
        }
    }

    fun onForecastCommand(city: String) {
        val forecast = api.getForecast(city, language)
        if (forecast == null) {
            sender.sendText(chatId, "Не знаю такого города")
        } else {
            showForecast(chatId, forecast, locale)
        }
    }

    fun onAddCity(city: String) {
        val weather = api.getWeather(city, language)
        if (weather == null) {
            sender.sendText(chatId, "Не знаю такого города")
        } else {
            repository.putCity(chatId, weather.id)
            showWeather(chatId, language)
        }
    }

    fun onDeleteCity(city: String) {
        val temperature = api.getWeather(city, language)
        when {
            temperature == null -> sender.sendText(chatId, "Не знаю такого города")
            repository.deleteCity(chatId, temperature.id) -> sender.sendText(chatId, "Удалено")
            else -> sender.sendText(chatId, "Нет такого города")
        }
    }

    fun onCelsiusCommand() {
        sender.sendText(chatId, "Можешь звать меня просто Андерс")
    }

    fun onWeatherCommand() {
        showWeather(chatId, language)
    }

    private fun showWeather(chatId: Long, language: String?) {
        val dbCities = repository.getCities(chatId)
        val cities = dbCities.ifEmpty { listOf(DEFAULT_CITY_ID) }
        val temperatures = api.getWeathers(cities, language)
        check(temperatures.isNotEmpty())
        sender.sendText(chatId, temperatures.joinToString("\n") { it.format() })
    }

    private fun showForecast(chatId: Long, forecast: Forecast, locale: Locale) {
        val city = forecast.city.name
        ggsave(plotForecast(forecast, locale), "$city.png")
        sender.sendPhoto(chatId, File("lets-plot-images/$city.png"))
    }

}
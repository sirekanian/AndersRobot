package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.api.Forecast
import com.sirekanyan.andersrobot.api.WeatherApi
import com.sirekanyan.andersrobot.command.Command
import com.sirekanyan.andersrobot.extensions.logError
import com.sirekanyan.andersrobot.extensions.sendPhoto
import com.sirekanyan.andersrobot.extensions.sendText
import com.sirekanyan.andersrobot.extensions.sendWeather
import com.sirekanyan.andersrobot.image.generateImage
import com.sirekanyan.andersrobot.image.plotForecast
import com.sirekanyan.andersrobot.repository.CityRepository
import com.sirekanyan.andersrobot.repository.supportedLanguages
import jetbrains.letsPlot.export.ggsave
import org.telegram.telegrambots.meta.api.objects.Location
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import java.io.File
import java.util.*
import javax.imageio.ImageIO

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

    fun onForecastCommand(city: String) {
        val forecast = api.getForecast(city, language)
        if (forecast == null) {
            sender.sendText(chatId, "Не знаю такого города")
        } else {
            showForecast(chatId, forecast, locale)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onCelsiusCommand(command: Command) {
        sender.sendText(chatId, "Можешь звать меня просто Андерс")
    }

    @Suppress("UNUSED_PARAMETER")
    fun onWeatherCommand(command: Command) {
        showWeather(chatId, language)
    }

    fun onCityMissing(command: Command) {
        delayedCommands[chatId] = command
        sender.sendText(chatId, "Какой город?")
    }

    private fun showWeather(chatId: Long, language: String?) {
        val dbCities = repository.getCities(chatId)
        val cities = dbCities.ifEmpty { listOf(DEFAULT_CITY_ID) }
        val weathers = api.getWeathers(cities, language)
        check(weathers.isNotEmpty())
        weathers.singleOrNull()?.let { weather ->
            sender.sendWeather(chatId, weather)
            return
        }
        try {
            val file = File("weather-$chatId.png")
            ImageIO.write(generateImage(weathers), "png", file)
            sender.sendPhoto(chatId, file)
        } catch (exception: Exception) {
            sender.logError("Cannot send image to $chatId", exception)
            sender.sendText(chatId, weathers.joinToString("\n") { it.format() })
        }
    }

    private fun showForecast(chatId: Long, forecast: Forecast, locale: Locale) {
        val city = forecast.city.name
        ggsave(plotForecast(forecast, locale), "$city.png")
        sender.sendPhoto(chatId, File("lets-plot-images/$city.png"))
    }

}
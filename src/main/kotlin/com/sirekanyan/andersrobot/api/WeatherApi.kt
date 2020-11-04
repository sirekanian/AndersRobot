package com.sirekanyan.andersrobot.api

import com.sirekanyan.andersrobot.config.Config
import com.sirekanyan.andersrobot.config.ConfigKey.WEATHER_API_KEY
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private const val WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather"
private val CITIES = listOf(
    "Miass",
    "Chelyabinsk",
    "Moscow",
    "Yerevan",
    "Boston",
)

class WeatherApi {

    private val httpClient = HttpClient()
    private val apiKey = Config[WEATHER_API_KEY]

    fun getTemperature(city: String, accuracy: Int): String? = runBlocking {
        val weather = getWeather(city)
        weather?.let {
            formatWeather(weather, accuracy) + " — " + formatCity(weather)
        }
    }

    fun getTemperatures(accuracy: Int): List<String> = runBlocking {
        CITIES.associateWith { city ->
            async { getWeather(city) }
        }.map { (city, weather) ->
            weather.await()?.let {
                formatWeather(it, accuracy) + " — $city"
            }
        }.filterNotNull()
    }

    private suspend fun getWeather(city: String): Weather? =
        try {
            println("getting $city")
            val response: String = httpClient.get(WEATHER_URL) {
                parameter("q", city)
                parameter("units", "metric")
                parameter("appid", apiKey)
            }
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString(response)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }

    private fun formatWeather(weather: Weather, accuracy: Int): String =
        "%.${accuracy}f°C".format(weather.main.temp)

    private fun formatCity(weather: Weather): String =
        "${weather.name} (${weather.sys.country})"

}
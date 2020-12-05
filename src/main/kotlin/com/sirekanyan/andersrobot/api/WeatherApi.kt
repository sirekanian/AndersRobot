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

class WeatherApi {

    private val httpClient = HttpClient()
    private val apiKey = Config[WEATHER_API_KEY]
    private val comparator = compareBy<Weather> { it.sys.country.toLowerCase().replace("ru", "aa") }.thenBy { it.name }

    fun getTemperature(city: String, language: String?): Weather? = runBlocking {
        getWeather(city, language)
    }

    fun getTemperatures(cities: List<String>, accuracy: Int, language: String?): List<String> = runBlocking {
        cities.map { city -> async { getWeather(city, language) } }
            .mapNotNull { it.await() }
            .sortedWith(comparator)
            .map { it.format(accuracy) }
    }

    private suspend fun getWeather(city: String, language: String?): Weather? =
        try {
            println("getting $city")
            val response: String = httpClient.get(WEATHER_URL) {
                parameter("q", city)
                parameter("units", "metric")
                if (language != null) {
                    parameter("lang", language)
                }
                parameter("appid", apiKey)
            }
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString(response)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }

}
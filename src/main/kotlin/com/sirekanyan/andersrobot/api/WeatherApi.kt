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
        println("getting $city")
        getWeather("q" to city, "lang" to language)
    }

    fun getTemperatures(cities: List<Long>, language: String?): List<Weather> = runBlocking {
        println("getting $cities")
        cities.map { city -> async { getWeather("id" to city, "lang" to language) } }
            .mapNotNull { it.await() }
            .sortedWith(comparator)
    }

    private suspend fun getWeather(vararg params: Pair<String, Any?>): Weather? =
        try {
            val response: String = httpClient.get(WEATHER_URL) {
                params.forEach { (k, v) ->
                    if (v != null) {
                        parameter(k, v)
                    }
                }
                parameter("units", "metric")
                parameter("appid", apiKey)
            }
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString(response)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }

}
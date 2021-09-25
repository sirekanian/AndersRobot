package com.sirekanyan.andersrobot.extensions

import com.sirekanyan.andersrobot.botName
import kotlin.text.RegexOption.IGNORE_CASE

private val REGEX = Regex("\\b(андерс|anders|погод[аеуы])\\b", IGNORE_CASE)
private val DEGREE_REGEX = Regex("\\bградус", IGNORE_CASE)
private val CELSIUS_REGEX = Regex("\\b(Celsi|Цельси)", IGNORE_CASE)
private fun commandRegex(en: String, ru: String) = Regex("($en(@${botName})?|$ru) +(.+)", IGNORE_CASE)
private val CITY_REGEX = commandRegex("/temp", "погода")
private val FORECAST_REGEX = commandRegex("/forecast", "прогноз")
private val ADD_CITY_REGEX = commandRegex("/add", "добавить город")
private val DEL_CITY_REGEX = commandRegex("/del", "удалить город")

fun isWeatherCommand(text: String?): Boolean =
    text?.contains(REGEX) == true || text?.contains(DEGREE_REGEX) == true ||
            text == "/temp" || text == "/temp@$botName"

fun isCelsiusCommand(text: String?): Boolean =
    text?.contains(CELSIUS_REGEX) == true

fun getCityCommand(text: String?): String? =
    CITY_REGEX.matchCommand(text)

fun getForecastCommand(text: String?): String? =
    FORECAST_REGEX.matchCommand(text)

fun getAddCityCommand(text: String?): String? =
    ADD_CITY_REGEX.matchCommand(text)

fun getDelCityCommand(text: String?): String? =
    DEL_CITY_REGEX.matchCommand(text)

private fun Regex.matchCommand(text: String?): String? =
    matchEntire(text.orEmpty())?.groupValues?.get(3)
package com.sirekanyan.andersrobot.extensions

import com.sirekanyan.andersrobot.botName
import kotlin.text.RegexOption.IGNORE_CASE

private val REGEX = Regex("\\b(андерс|anders|погод[аеуы])\\b", IGNORE_CASE)
private val DEGREE_REGEX = Regex("\\bградус", IGNORE_CASE)
private val CELSIUS_REGEX = Regex("\\b(Celsi|Цельси)", IGNORE_CASE)
private val CITY_REGEX = Regex("(/temp|погода) +(.+)", IGNORE_CASE)
private val FORECAST_REGEX = Regex("(/forecast|прогноз) +(.+)", IGNORE_CASE)
private val ADD_CITY_REGEX = Regex("(/add|добавить город) +(.+)", IGNORE_CASE)
private val DEL_CITY_REGEX = Regex("(/del|удалить город) +(.+)", IGNORE_CASE)

fun isWeatherCommand(text: String?): Boolean =
    text?.contains(REGEX) == true || text?.contains(DEGREE_REGEX) == true ||
            text == "/temp" || text == "/temp@$botName"

fun isCelsiusCommand(text: String?): Boolean =
    text?.contains(CELSIUS_REGEX) == true

fun getCityCommand(text: String?): String? =
    CITY_REGEX.matchEntire(text.orEmpty())?.groupValues?.get(2)

fun getForecastCommand(text: String?): String? =
    FORECAST_REGEX.matchEntire(text.orEmpty())?.groupValues?.get(2)

fun getAddCityCommand(text: String?): String? =
    ADD_CITY_REGEX.matchEntire(text.orEmpty())?.groupValues?.get(2)

fun getDelCityCommand(text: String?): String? =
    DEL_CITY_REGEX.matchEntire(text.orEmpty())?.groupValues?.get(2)

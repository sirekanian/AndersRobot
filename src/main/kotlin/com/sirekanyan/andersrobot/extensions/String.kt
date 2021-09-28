package com.sirekanyan.andersrobot.extensions

import com.sirekanyan.andersrobot.botName
import kotlin.text.RegexOption.IGNORE_CASE

private val REGEX = Regex("\\b(андерс|anders|погод[аеуы])\\b", IGNORE_CASE)
private val DEGREE_REGEX = Regex("\\bградус", IGNORE_CASE)
private val CELSIUS_REGEX = Regex("\\b(Celsi|Цельси)", IGNORE_CASE)

fun isWeatherCommand(text: String?): Boolean =
    text?.contains(REGEX) == true || text?.contains(DEGREE_REGEX) == true ||
            text == "/temp" || text == "/temp@$botName"

fun isCelsiusCommand(text: String?): Boolean =
    text?.contains(CELSIUS_REGEX) == true

fun parseCityArgument(text: String?, en: String, ru: String): String? {
    val regex = Regex("($en(@${botName})?|$ru) +(.+)", IGNORE_CASE)
    return regex.matchEntire(text.orEmpty())?.groupValues?.get(3)
}

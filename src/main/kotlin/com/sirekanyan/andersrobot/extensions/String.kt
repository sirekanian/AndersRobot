package com.sirekanyan.andersrobot.extensions

import com.sirekanyan.andersrobot.botName
import kotlin.text.RegexOption.IGNORE_CASE

private val REGEX = Regex("\\b(андерс|anders|погод[аеуы])\\b", IGNORE_CASE)

fun isWeatherCommand(text: String?): Boolean =
    text?.contains(REGEX) == true || text == "/temp" || text == "/temp@$botName"

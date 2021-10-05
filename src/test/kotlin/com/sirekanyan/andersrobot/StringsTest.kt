package com.sirekanyan.andersrobot

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.text.RegexOption.IGNORE_CASE

class StringsTest {

    @Test
    fun isWeatherCommandTest1() {
        assertEquals(true, isWeatherCommand("Сколько градусов?"))
        assertEquals(true, isWeatherCommand("Кто разбил градусник?"))
        assertEquals(true, isWeatherCommand("погода"))
        assertEquals(true, isWeatherCommand("ПОГОДА"))
        assertEquals(true, isWeatherCommand("Погода"))
        assertEquals(true, isWeatherCommand("ПоГоДа"))
        assertEquals(true, isWeatherCommand("ПоГоДа!!!"))
        assertEquals(true, isWeatherCommand("скажи погоду"))
        assertEquals(true, isWeatherCommand("какая погода"))
        assertEquals(true, isWeatherCommand("что по погоде"))
        assertEquals(true, isPrimaryWeatherCommand("/temp"))
        assertEquals(true, isPrimaryWeatherCommand("/TEMP"))
        assertEquals(true, isPrimaryWeatherCommand("/temp@AndersRobot"))
    }

    @Test
    fun isWeatherCommandTest2() {
        assertEquals(false, isWeatherCommand("какая непогода"))
        assertEquals(false, isWeatherCommand("погоди"))
        assertEquals(false, isWeatherCommand("погоди"))
        assertEquals(false, isWeatherCommand("погоди-ка"))
        assertEquals(false, isPrimaryWeatherCommand("temp"))
        assertEquals(false, isPrimaryWeatherCommand("/temperature"))
    }

    private fun isWeatherCommand(text: String) =
        text.contains(Regex("\\b((андерс|anders|погод[аеуы])\\b|градус)", IGNORE_CASE))

    private fun isPrimaryWeatherCommand(text: String) =
        text.matches(Regex("/temp(@$botName)?", IGNORE_CASE))

}
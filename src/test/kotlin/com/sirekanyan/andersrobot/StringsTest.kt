package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.extensions.isWeatherCommand
import org.junit.Assert.assertEquals
import org.junit.Test

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
        assertEquals(true, isWeatherCommand("/temp"))
        assertEquals(true, isWeatherCommand("/temp@AndersRobot"))
    }

    @Test
    fun isWeatherCommandTest2() {
        assertEquals(false, isWeatherCommand("какая непогода"))
        assertEquals(false, isWeatherCommand("погоди"))
        assertEquals(false, isWeatherCommand("погоди"))
        assertEquals(false, isWeatherCommand("погоди-ка"))
        assertEquals(false, isWeatherCommand("/TEMP"))
        assertEquals(false, isWeatherCommand("temp"))
        assertEquals(false, isWeatherCommand("/temperature"))
    }

}
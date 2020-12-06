package com.sirekanyan.andersrobot.api

import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class Weather(
    val main: MainInfo,
    val id: Long,
    val name: String,
    val sys: System,
    val weather: List<Condition>
) {

    @Serializable
    data class MainInfo(val temp: Double)

    @Serializable
    data class System(val country: String)

    @Serializable
    data class Condition(val id: Int)

    fun findImageFile(): File? {
        val w = weather.firstOrNull() ?: return null
        return File("data/${w.id}.png").takeIf { it.exists() }
    }

    fun format(accuracy: Int): String =
        formatTemperature(accuracy) + " — $name"

    private fun formatTemperature(accuracy: Int): String =
        "%.${accuracy}f°C".format(main.temp)

}
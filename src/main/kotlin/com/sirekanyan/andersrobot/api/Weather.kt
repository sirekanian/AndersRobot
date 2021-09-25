package com.sirekanyan.andersrobot.api

import kotlinx.serialization.Serializable
import java.io.File
import kotlin.math.roundToInt

@Serializable
data class Weather(
    val main: MainInfo,
    val id: Long,
    val name: String,
    val sys: System,
    val weather: List<Condition>
) {

    @Serializable
    data class System(val country: String = "")

    @Serializable
    data class Condition(val id: Int)

    fun findImageFile(): File? {
        val w = weather.firstOrNull() ?: return null
        return File("data/${w.id}.webp").takeIf { it.exists() }
    }

    fun format(): String =
        "$name ${main.temp.roundToInt()}°C"

}
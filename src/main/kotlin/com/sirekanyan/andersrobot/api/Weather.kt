package com.sirekanyan.andersrobot.api

import com.sirekanyan.andersrobot.image.FileProvider
import com.sirekanyan.andersrobot.image.ImageProvider
import kotlinx.serialization.Serializable
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import kotlin.math.roundToInt

const val WIDTH = 504
const val HEIGHT = 64
private val fileProvider = FileProvider("webp")
private val imageProvider = ImageProvider("png", WIDTH, HEIGHT)
private val lightBackgroundIds = setOf(210, 212, 221, 504, 611, 701, 711, 721, 731, 800, 803, 902, 903)

@Serializable
data class Weather(
    val main: MainInfo,
    val id: Long,
    val name: String,
    val sys: System,
    val weather: List<Condition>,
) {

    val temperature: String
        get() = "${main.temp.roundToInt()}°C"

    private val primaryCondition: Condition?
        get() = weather.firstOrNull()

    @Serializable
    data class System(val country: String = "")

    @Serializable
    data class Condition(val id: Int)

    fun findStickerFile(): File? {
        val id = primaryCondition?.id ?: return null
        return fileProvider.findFile(id)
    }

    fun findBackgroundImage(): BufferedImage? {
        val id = primaryCondition?.id ?: return null
        return imageProvider.findImage(id)
    }

    fun format(): String =
        "$name $temperature"

    fun getTextColor(): Color =
        if (primaryCondition?.id in lightBackgroundIds) Color.BLACK else Color.WHITE

}
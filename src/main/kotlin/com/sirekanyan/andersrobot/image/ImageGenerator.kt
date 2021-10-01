package com.sirekanyan.andersrobot.image

import com.sirekanyan.andersrobot.api.HEIGHT
import com.sirekanyan.andersrobot.api.WIDTH
import com.sirekanyan.andersrobot.api.Weather
import java.awt.Font
import java.awt.Font.TRUETYPE_FONT
import java.awt.FontMetrics
import java.awt.Graphics2D
import java.awt.RenderingHints.KEY_TEXT_ANTIALIASING
import java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File

private const val PADDING = 24
private val montserratFont = Font.createFont(TRUETYPE_FONT, File("data/Montserrat-Light.ttf"))
private val cityFont = montserratFont.deriveFont(32f)
private val tempFont = montserratFont.deriveFont(42f)

fun generateImage(weathers: List<Weather>): BufferedImage {
    val image = BufferedImage(WIDTH, HEIGHT * weathers.size, TYPE_INT_RGB)
    val graphics = image.createGraphics()
    graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON)
    graphics.drawWeatherImages(weathers)
    graphics.dispose()
    return image
}

private fun Graphics2D.drawWeatherImages(weathers: List<Weather>) {
    weathers.forEachIndexed { index, weather ->
        weather.findBackgroundImage()?.let { image ->
            drawWeatherImage(index, image)
        }
        drawWeatherText(index, weather)
    }
}

private fun Graphics2D.drawWeatherImage(index: Int, image: BufferedImage) {
    drawImage(image, 0, HEIGHT * index, null)
}

private fun Graphics2D.drawWeatherText(index: Int, weather: Weather) {
    color = weather.getTextColor()
    font = cityFont
    drawCityText(index, weather.name)
    font = tempFont
    drawTempText(index, weather.temperature)
}

private fun Graphics2D.drawCityText(index: Int, text: String) {
    val x = PADDING
    val y = fontMetrics.textY(index)
    drawString(text, x, y)
}

private fun Graphics2D.drawTempText(index: Int, text: String) {
    val x = WIDTH - PADDING - fontMetrics.stringWidth(text)
    val y = fontMetrics.textY(index)
    drawString(text, x, y)
}

private fun FontMetrics.textY(index: Int): Int =
    (HEIGHT - height) / 2 + ascent + HEIGHT * index

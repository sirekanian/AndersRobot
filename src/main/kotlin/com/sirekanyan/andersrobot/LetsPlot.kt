package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.api.Forecast
import jetbrains.datalore.base.values.Color
import jetbrains.letsPlot.geom.geomHLine
import jetbrains.letsPlot.geom.geomSmooth
import jetbrains.letsPlot.geom.geomVLine
import jetbrains.letsPlot.ggsize
import jetbrains.letsPlot.intern.Plot
import jetbrains.letsPlot.label.ggtitle
import jetbrains.letsPlot.letsPlot
import jetbrains.letsPlot.scale.scaleXContinuous
import jetbrains.letsPlot.scale.scaleYContinuous
import java.lang.System.currentTimeMillis
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

fun plotForecast(forecast: Forecast, locale: Locale): Plot {
    val now = currentTimeMillis() / 1000
    val offset = ZoneOffset.ofTotalSeconds(forecast.city.timezone)
    val values = forecast.list.take(33)
    val xValues = values.map { it.dt }
    val yValues = values.map { it.main.temp }
    val xBreaks = xBreaks(now, xValues, offset)
    val yBreaks = yBreaks(yValues)
    val xLabels = xBreaks.map { it.toLocalDateTime(offset).formatDateTime("d MMM", locale) }
    val current = now.toLocalDateTime(offset).formatDateTime("d MMMM, HH:mm", locale)
    return letsPlot(mapOf("x" to xValues, "y" to yValues)) { x = "x"; y = "y" } +
            ggsize(300, 200) +
            ggtitle("${forecast.city.name}, $current") +
            geomSmooth(method = "loess", se = false, span = 2.0 / values.size, color = Color.BLUE) +
            scaleXContinuous("", breaks = xBreaks, labels = xLabels) +
            scaleYContinuous("", breaks = yBreaks, format = "{d}°C") +
            geomVLine(data = mapOf("x" to xBreaks), linetype = "dotted") { xintercept = "x" } +
            geomHLine(data = mapOf("y" to yBreaks), linetype = "dotted") { yintercept = "y" }
}

private fun xBreaks(now: Long, values: List<Long>, offset: ZoneOffset): List<Long> {
    var br = now.toLocalDateTime(offset).toLocalDate().plusDays(1)
    val end = checkNotNull(values.maxOrNull()).toLocalDateTime(offset).toLocalDate()
    val breaks = mutableListOf<Long>()
    while (br <= end) {
        breaks.add(br.toEpochSecond(LocalTime.MIN, offset))
        br = br.plusDays(1)
    }
    return breaks
}

private fun yBreaks(values: List<Double>): List<Int> {
    var br = floor(checkNotNull(values.minOrNull()) / 5).toInt() * 5
    val end = ceil(checkNotNull(values.maxOrNull()) / 5).toInt() * 5
    val breaks = mutableListOf<Int>()
    while (br <= end) {
        breaks.add(br)
        br += 5
    }
    return breaks
}

private fun Long.toLocalDateTime(offset: ZoneOffset) =
    LocalDateTime.ofEpochSecond(this, 0, offset)

private fun LocalDateTime.formatDateTime(pattern: String, locale: Locale) =
    format(DateTimeFormatter.ofPattern(pattern, locale))

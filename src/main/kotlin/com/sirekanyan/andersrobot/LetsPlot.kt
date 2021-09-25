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

fun plotForecast(forecast: Forecast): Plot {
    val offset = ZoneOffset.ofTotalSeconds(forecast.city.timezone)
    val values = forecast.list.take(33)
    val xValues = values.map { it.dt }
    val yValues = values.map { it.main.temp }
    val xBreaks = xBreaks(xValues, offset)
    val yBreaks = yBreaks(yValues)
    val xLabels = xBreaks.map { it.toLocalDateTime(offset).formatDateTime("d MMM") }
    val current = (currentTimeMillis() / 1000).toLocalDateTime(offset).formatDateTime("d MMMM, HH:mm")
    return letsPlot(mapOf("x" to xValues, "y" to yValues)) { x = "x"; y = "y" } +
            ggsize(400, 250) +
            ggtitle("${forecast.city.name}, $current") +
            geomSmooth(method = "loess", se = false, span = 2.0 / values.size, color = Color.BLUE) +
            scaleXContinuous("", breaks = xBreaks, labels = xLabels) +
            scaleYContinuous("", breaks = yBreaks, format = "{d}°C") +
            geomVLine(data = mapOf("x" to xBreaks), linetype = "dotted") { xintercept = "x" } +
            geomHLine(data = mapOf("y" to yBreaks), linetype = "dotted", color = Color.GRAY) { yintercept = "y" }
}

private fun xBreaks(times: List<Long>, offset: ZoneOffset): List<Long> {
    val start = times.minOrNull()!!.toLocalDateTime(offset).toLocalDate()
    val end = times.maxOrNull()!!.toLocalDateTime(offset).toLocalDate()
    val breaks = mutableListOf<Long>()
    var b = start.plusDays(1)
    while (b <= end) {
        breaks.add(b.toEpochSecond(LocalTime.MIN, offset))
        b = b.plusDays(1)
    }
    return breaks
}

private fun yBreaks(temps: List<Double>): List<Int> {
    val start = (temps.minOrNull()!!.toInt() / 5 - 1) * 5
    val end = (temps.maxOrNull()!!.toInt() / 5 + 1) * 5
    val breaks = mutableListOf<Int>()
    var b = start + 5
    while (b <= end) {
        breaks.add(b)
        b += 5
    }
    return breaks
}

private fun Long.toLocalDateTime(offset: ZoneOffset) =
    LocalDateTime.ofEpochSecond(this, 0, offset)

private fun LocalDateTime.formatDateTime(pattern: String) =
    format(DateTimeFormatter.ofPattern(pattern))

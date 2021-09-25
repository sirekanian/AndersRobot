package com.sirekanyan.andersrobot.api

import kotlinx.serialization.Serializable

@Serializable
data class Forecast(val cnt: Int, val list: List<ForecastItem>, val city: City) {

    @Serializable
    data class ForecastItem(val dt: Long, val main: MainInfo)

    @Serializable
    data class City(val name: String, val timezone: Int)

}
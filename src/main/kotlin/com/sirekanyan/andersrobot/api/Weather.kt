package com.sirekanyan.andersrobot.api

import kotlinx.serialization.Serializable

@Serializable
data class Weather(val main: MainInfo, val name: String, val sys: Sys) {

    @Serializable
    data class MainInfo(val temp: Double)

    @Serializable
    data class Sys(val country: String)

}
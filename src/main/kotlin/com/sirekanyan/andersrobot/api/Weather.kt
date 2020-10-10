package com.sirekanyan.andersrobot.api

import kotlinx.serialization.Serializable

@Serializable
data class Weather(val main: MainInfo) {

    @Serializable
    data class MainInfo(val temp: Double)

}
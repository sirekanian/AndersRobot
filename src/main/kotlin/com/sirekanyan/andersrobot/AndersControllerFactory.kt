package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.api.WeatherApi
import com.sirekanyan.andersrobot.config.Config
import com.sirekanyan.andersrobot.config.ConfigKey.DB_URL
import com.sirekanyan.andersrobot.repository.CityRepositoryImpl
import org.telegram.telegrambots.meta.api.objects.Update

class AndersControllerFactory {

    private val api = WeatherApi()
    private val repository = CityRepositoryImpl(Config[DB_URL])

    fun createController(sender: AndersRobot, update: Update): AndersController =
        AndersController(api, repository, sender, update)

}
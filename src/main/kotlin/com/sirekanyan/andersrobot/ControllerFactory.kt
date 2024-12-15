package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.api.WeatherApi
import com.sirekanyan.andersrobot.config.ConfigKey.DB_URL
import com.sirekanyan.andersrobot.repository.CityRepositoryImpl
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.TelegramClient

class ControllerFactory {

    private val api = WeatherApi()
    private val repository = CityRepositoryImpl(Config[DB_URL])

    fun createController(client: TelegramClient, update: Update): Controller =
        Controller(api, repository, client, update)

}
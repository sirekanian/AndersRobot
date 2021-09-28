package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.extensions.*
import org.telegram.telegrambots.meta.api.objects.Message

val userCommands =
    listOf(
        LocationCommand,
        CityCommand,
        ForecastCommand,
        AddCityCommand,
        DeleteCityCommand,
        CelsiusCommand,
        WeatherCommand,
    )

interface Command {
    fun execute(controller: AndersController, message: Message): Boolean
}

object LocationCommand : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        if (message.hasLocation()) {
            controller.onLocationCommand(message.location)
            return true
        }
        return false
    }
}

object CityCommand : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        val city = getCityCommand(message.text)
        when {
            city.isNullOrEmpty() -> return false
            else -> controller.onCityCommand(city)
        }
        return true
    }
}

object ForecastCommand : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        val city = getForecastCommand(message.text)
        when {
            city.isNullOrEmpty() -> return false
            else -> controller.onForecastCommand(city)
        }
        return true
    }
}

object AddCityCommand : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        val city = getAddCityCommand(message.text)
        when {
            city.isNullOrEmpty() -> return false
            else -> controller.onAddCity(city)
        }
        return true
    }
}

object DeleteCityCommand : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        val city = getDelCityCommand(message.text)
        when {
            city.isNullOrEmpty() -> return false
            else -> controller.onDeleteCity(city)
        }
        return true
    }
}

object CelsiusCommand : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        if (isCelsiusCommand(message.text)) {
            controller.onCelsiusCommand()
            return true
        }
        return false
    }
}

object WeatherCommand : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        if (isWeatherCommand(message.text)) {
            controller.onWeatherCommand()
            return true
        }
        return false
    }
}

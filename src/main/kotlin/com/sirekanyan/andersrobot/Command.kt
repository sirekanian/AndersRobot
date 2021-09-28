package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.extensions.isCelsiusCommand
import com.sirekanyan.andersrobot.extensions.isWeatherCommand
import com.sirekanyan.andersrobot.extensions.parseCityArgument
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
        val city = parseCityArgument(message.text, "/temp", "погода")
        when {
            city.isNullOrEmpty() -> return false
            else -> controller.onCityCommand(city)
        }
        return true
    }
}

object ForecastCommand : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        val city = parseCityArgument(message.text, "/forecast", "прогноз")
        when {
            city.isNullOrEmpty() -> return false
            else -> controller.onForecastCommand(city)
        }
        return true
    }
}

object AddCityCommand : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        val city = parseCityArgument(message.text, "/add", "добавить город")
        when {
            city.isNullOrEmpty() -> return false
            else -> controller.onAddCity(city)
        }
        return true
    }
}

object DeleteCityCommand : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        val city = parseCityArgument(message.text, "/del", "удалить город")
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

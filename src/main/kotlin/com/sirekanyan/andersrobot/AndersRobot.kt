package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.command.CityCommand
import com.sirekanyan.andersrobot.command.Command
import com.sirekanyan.andersrobot.command.LocationCommand
import com.sirekanyan.andersrobot.command.RegexCommand
import com.sirekanyan.andersrobot.config.Config
import com.sirekanyan.andersrobot.config.ConfigKey.*
import com.sirekanyan.andersrobot.extensions.logError
import com.sirekanyan.andersrobot.extensions.logInfo
import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.util.WebhookUtils

val botName = Config[BOT_USERNAME]
val adminId = Config[ADMIN_ID].toLong()
val delayedCommands = mutableMapOf<Long, Command>()
private val primaryCommands: List<Command> =
    listOf(
        LocationCommand,
        CityCommand("/temp(@$botName)?", AndersController::onCityCommand, AndersController::onWeatherCommand),
        CityCommand("/add(@$botName)?", AndersController::onAddCity, AndersController::onCityMissing),
        CityCommand("/delete(@$botName)?", AndersController::onDeleteCity, AndersController::onCityMissing),
        CityCommand("/forecast(@$botName)?", AndersController::onForecastCommand, AndersController::onCityMissing),
    )
private val secondaryCommands: List<Command> =
    listOf(
        CityCommand("погода", AndersController::onCityCommand, AndersController::onWeatherCommand),
        CityCommand("добавить город", AndersController::onAddCity, AndersController::onCityMissing),
        CityCommand("удалить город", AndersController::onDeleteCity, AndersController::onCityMissing),
        CityCommand("прогноз", AndersController::onForecastCommand, AndersController::onCityMissing),
        RegexCommand("\\b(celsi|цельси)", AndersController::onCelsiusCommand),
        RegexCommand("\\b((андерс|anders|погод[аеуы])\\b|градус)", AndersController::onWeatherCommand),
    )

class AndersRobot : DefaultAbsSender(DefaultBotOptions()), LongPollingBot {

    private val factory = AndersControllerFactory()

    override fun getBotUsername(): String = botName

    override fun getBotToken(): String = Config[BOT_TOKEN]

    override fun onUpdateReceived(update: Update) {
        try {
            onUpdate(update)
        } catch (exception: Exception) {
            logError("Cannot handle update", exception)
            logError(update)
        }
    }

    private fun onUpdate(update: Update) {
        val message = update.message ?: run {
            println("Update is ignored: message is empty")
            return
        }
        val chatId = message.chatId
        val text = message.text
        println("${message.from?.id} (chat $chatId) => $text")
        val controller = factory.createController(this, update)
        for (command in primaryCommands) {
            if (command.execute(controller, message)) {
                return
            }
        }
        delayedCommands[chatId]?.let { command ->
            delayedCommands.remove(chatId)
            command.execute(controller, text)
            return
        }
        for (command in secondaryCommands) {
            if (command.execute(controller, message)) {
                return
            }
        }
    }

    override fun clearWebhook() {
        logInfo("Cleared.")
        WebhookUtils.clearWebhook(this)
    }

    override fun onClosing() {
        logInfo("Closed.")
    }

}
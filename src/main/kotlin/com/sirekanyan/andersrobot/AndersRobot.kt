package com.sirekanyan.andersrobot

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
        val message = update.message
        if (update.hasMessage()) {
            println("${message.from?.id} (chat ${message.chatId}) => ${message.text}")
        } else {
            println("Update is ignored: message is empty")
            return
        }
        val controller = factory.createController(this, update)
        for (command in userCommands) {
            if (command.execute(controller, update.message)) {
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
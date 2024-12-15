@file:JvmName("Main")

package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.config.ConfigKey
import com.sirekanyan.andersrobot.config.ConfigKey.BOT_TOKEN
import org.sirekanyan.telegrambots.BotConfig
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication

val Config = BotConfig(ConfigKey.entries)
private val BotToken = Config[BOT_TOKEN]

fun main() {
    TelegramBotsLongPollingApplication().registerBot(BotToken, AndersRobot(BotToken))
}
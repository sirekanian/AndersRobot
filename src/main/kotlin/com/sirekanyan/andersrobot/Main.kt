@file:JvmName("Main")

package com.sirekanyan.andersrobot

import com.sirekanyan.andersrobot.config.ConfigKey
import org.sirekanyan.telegrambots.BotConfig
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

val Config = BotConfig(ConfigKey.entries)

fun main() {
    TelegramBotsApi(DefaultBotSession::class.java).registerBot(AndersRobot())
}
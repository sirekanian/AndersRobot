@file:JvmName("Main")

package com.sirekanyan.andersrobot

import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

fun main() {
    TelegramBotsApi(DefaultBotSession::class.java).registerBot(AndersRobot())
}
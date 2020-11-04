package com.sirekanyan.andersrobot.extensions

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

fun AbsSender.sendText(chatId: Long, text: String): Message =
    execute(SendMessage(chatId, text))

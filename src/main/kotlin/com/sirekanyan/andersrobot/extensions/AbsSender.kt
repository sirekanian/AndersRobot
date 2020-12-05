package com.sirekanyan.andersrobot.extensions

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import java.io.File

fun AbsSender.sendText(chatId: Long, text: String): Message =
    execute(SendMessage(chatId, text))

fun AbsSender.sendPhoto(chatId: Long, file: File, caption: String) {
    execute(SendPhoto().setChatId(chatId).setPhoto(file).setCaption(caption))
}

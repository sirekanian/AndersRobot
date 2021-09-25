package com.sirekanyan.andersrobot.extensions

import com.sirekanyan.andersrobot.api.Weather
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.send.SendSticker
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import java.io.File
import java.util.concurrent.ConcurrentHashMap

private val cachedFileIds: MutableMap<File, String> = ConcurrentHashMap()

fun AbsSender.sendText(chatId: Long, text: String): Message =
    execute(SendMessage(chatId, text))

fun AbsSender.sendPhoto(chatId: Long, file: File): Message =
    execute(SendPhoto().setChatId(chatId).setPhoto(file))

fun AbsSender.sendWeather(chatId: Long, weather: Weather) {
    weather.findImageFile()?.let { icon ->
        sendSticker(chatId, icon)
    }
    sendText(chatId, weather.format())
}

private fun AbsSender.sendSticker(chatId: Long, file: File) {
    fun send(f: InputFile) = execute(SendSticker().setChatId(chatId).setSticker(f))
    val cachedFileId = cachedFileIds[file]
    if (cachedFileId == null) {
        println("sending file $file")
        val message = send(InputFile(file, file.name))
        cachedFileIds[file] = message.sticker.fileId
    } else {
        println("sending fileId $cachedFileId")
        send(InputFile(cachedFileId))
    }
}

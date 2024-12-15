package com.sirekanyan.andersrobot.extensions

import org.telegram.telegrambots.meta.api.methods.send.SendSticker
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.bots.AbsSender
import java.io.File
import java.util.concurrent.ConcurrentHashMap

private val cachedFileIds: MutableMap<File, String> = ConcurrentHashMap()

fun AbsSender.sendSticker(chatId: Long, file: File) {
    fun send(f: InputFile) = execute(SendSticker(chatId.toString(), f))
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

package com.sirekanyan.andersrobot.extensions

import com.sirekanyan.andersrobot.adminId
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.TelegramClient
import java.io.ByteArrayOutputStream
import java.io.PrintStream

fun TelegramClient.logInfo(text: String) {
    execute(SendMessage(adminId, text))
}

fun TelegramClient.logError(text: String, throwable: Throwable) {
    try {
        val stream = ByteArrayOutputStream()
        throwable.printStackTrace(PrintStream(stream))
        execute(SendMessage(adminId, "$text\n\n$stream"))
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}

fun TelegramClient.logError(update: Update) {
    try {
        val input = update.toString().byteInputStream()
        val document = InputFile(input, "update.txt")
        execute(SendDocument(adminId, document))
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}

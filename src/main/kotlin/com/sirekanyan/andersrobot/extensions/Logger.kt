package com.sirekanyan.andersrobot.extensions

import com.sirekanyan.andersrobot.adminId
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import java.io.ByteArrayOutputStream
import java.io.PrintStream

fun AbsSender.logInfo(text: String) {
    execute(SendMessage(adminId, text))
}

fun AbsSender.logError(text: String, throwable: Throwable) {
    try {
        val stream = ByteArrayOutputStream()
        throwable.printStackTrace(PrintStream(stream))
        execute(SendMessage(adminId, "$text\n\n$stream"))
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}

fun AbsSender.logError(update: Update) {
    try {
        val input = update.toString().byteInputStream()
        val document = InputFile(input, "update.txt")
        execute(SendDocument().setChatId(adminId).setDocument(document))
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}

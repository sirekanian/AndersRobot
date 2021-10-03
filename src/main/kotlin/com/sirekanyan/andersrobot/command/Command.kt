package com.sirekanyan.andersrobot.command

import com.sirekanyan.andersrobot.AndersController
import org.telegram.telegrambots.meta.api.objects.Message

interface Command {

    fun execute(controller: AndersController, message: Message): Boolean

    fun execute(controller: AndersController, arguments: String?): Boolean {
        throw UnsupportedOperationException()
    }

}
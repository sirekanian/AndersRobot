package com.sirekanyan.andersrobot.command

import com.sirekanyan.andersrobot.Controller
import org.telegram.telegrambots.meta.api.objects.message.Message

interface Command {

    fun execute(controller: Controller, message: Message): Boolean

    fun execute(controller: Controller, arguments: String?): Boolean {
        throw UnsupportedOperationException()
    }

}
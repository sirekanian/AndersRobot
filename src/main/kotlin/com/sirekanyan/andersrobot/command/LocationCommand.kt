package com.sirekanyan.andersrobot.command

import com.sirekanyan.andersrobot.AndersController
import org.telegram.telegrambots.meta.api.objects.Message

object LocationCommand : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        if (message.hasLocation()) {
            controller.onLocationCommand(message.location)
            return true
        }
        return false
    }
}
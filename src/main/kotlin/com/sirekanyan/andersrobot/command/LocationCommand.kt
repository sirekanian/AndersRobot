package com.sirekanyan.andersrobot.command

import com.sirekanyan.andersrobot.Controller
import org.telegram.telegrambots.meta.api.objects.Message

object LocationCommand : Command {
    override fun execute(controller: Controller, message: Message): Boolean {
        if (message.hasLocation()) {
            controller.onLocationCommand(message.location)
            return true
        }
        return false
    }
}
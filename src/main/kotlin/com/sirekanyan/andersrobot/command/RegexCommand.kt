package com.sirekanyan.andersrobot.command

import com.sirekanyan.andersrobot.AndersController
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.text.RegexOption.IGNORE_CASE

class RegexCommand(
    private val pattern: String,
    private val action: (AndersController) -> Unit,
) : Command {
    override fun execute(controller: AndersController, message: Message): Boolean {
        if (message.text?.contains(Regex(pattern, IGNORE_CASE)) == true) {
            action(controller)
            return true
        }
        return false
    }
}
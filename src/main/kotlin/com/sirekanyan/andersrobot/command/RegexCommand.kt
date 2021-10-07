package com.sirekanyan.andersrobot.command

import com.sirekanyan.andersrobot.Controller
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.text.RegexOption.IGNORE_CASE

class RegexCommand(
    private val pattern: String,
    private val action: (Controller, Command) -> Unit,
) : Command {
    override fun execute(controller: Controller, message: Message): Boolean {
        if (message.text?.contains(Regex(pattern, IGNORE_CASE)) == true) {
            action(controller, this)
            return true
        }
        return false
    }
}
package com.sirekanyan.andersrobot.command

import com.sirekanyan.andersrobot.AndersController
import org.telegram.telegrambots.meta.api.objects.Message

class CityCommand(
    private val pattern: String,
    private val action: (AndersController, String) -> Unit,
    private val onEmptyArguments: (AndersController, Command) -> Unit,
) : Command {

    override fun execute(controller: AndersController, message: Message): Boolean =
        execute(controller, parseCityArgument(message.text))

    override fun execute(controller: AndersController, arguments: String?): Boolean {
        when {
            arguments == null -> return false
            arguments.isBlank() -> onEmptyArguments(controller, this)
            else -> action(controller, arguments)
        }
        return true
    }

    private fun parseCityArgument(text: String?): String? {
        val regex = Regex("$pattern ?(.*)", RegexOption.IGNORE_CASE)
        return regex.matchEntire(text.orEmpty())?.groupValues?.last()
    }

}
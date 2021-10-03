package com.sirekanyan.andersrobot.command

import com.sirekanyan.andersrobot.AndersController
import com.sirekanyan.andersrobot.botName
import org.telegram.telegrambots.meta.api.objects.Message

class CityCommand(
    private val en: String,
    private val ru: String,
    private val action: (AndersController, String) -> Unit,
) : Command {

    override fun execute(controller: AndersController, message: Message): Boolean =
        execute(controller, parseCityArgument(message.text, en, ru))

    override fun execute(controller: AndersController, arguments: String?): Boolean {
        when {
            arguments == null -> return false
            arguments.isBlank() -> controller.onCityMissing(this)
            else -> action(controller, arguments)
        }
        return true
    }

    private fun parseCityArgument(text: String?, en: String, ru: String): String? {
        val regex = Regex("($en(@$botName)?|$ru) ?(.*)", RegexOption.IGNORE_CASE)
        return regex.matchEntire(text.orEmpty())?.groupValues?.get(3)
    }

}
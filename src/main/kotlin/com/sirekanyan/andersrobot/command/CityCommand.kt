package com.sirekanyan.andersrobot.command

import com.sirekanyan.andersrobot.AndersController
import com.sirekanyan.andersrobot.botName
import org.telegram.telegrambots.meta.api.objects.Message
import java.util.regex.Pattern
import kotlin.text.RegexOption.IGNORE_CASE

class CityCommand(
    private val englishWord: String,
    private val russianWord: String,
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
        val en = Pattern.quote(englishWord)
        val ru = Pattern.quote(russianWord)
        val regex = Regex("(/$en(@$botName)?|$ru) ?(.*)", IGNORE_CASE)
        return regex.matchEntire(text.orEmpty())?.groupValues?.last()
    }

}
package com.sirekanyan.andersrobot.command

import com.sirekanyan.andersrobot.Controller
import com.sirekanyan.andersrobot.botName
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.util.regex.Pattern
import kotlin.text.RegexOption.IGNORE_CASE

class CityCommand(
    private val words: List<String>,
    private val action: (Controller, String) -> Unit,
    private val onEmptyArguments: (Controller, Command) -> Unit,
) : Command {

    override fun execute(controller: Controller, message: Message): Boolean =
        execute(controller, parseCityArgument(message.text))

    override fun execute(controller: Controller, arguments: String?): Boolean {
        when {
            arguments == null -> return false
            arguments.isBlank() -> onEmptyArguments(controller, this)
            else -> action(controller, arguments)
        }
        return true
    }

    private fun parseCityArgument(text: String?): String? {
        val commands = words.flatMap { if (it.startsWith('/')) listOf(it, "$it@$botName") else listOf(it) }
        val regex = Regex("(${commands.joinToString("|", transform = Pattern::quote)})( (.*))?", IGNORE_CASE)
        return regex.matchEntire(text.orEmpty())?.groupValues?.last()
    }

}
package com.sirekanyan.andersrobot.config

import java.io.File
import java.util.*

private val IS_DEBUG = System.getProperty("debug") != null
private val CONFIG_FILE = if (IS_DEBUG) "bot.properties.debug" else "bot.properties"

object Config {

    private val properties = initProperties(initFile())

    private fun initFile(): File {
        val file = File(CONFIG_FILE)
        if (!file.exists()) {
            file.bufferedWriter().use { writer ->
                ConfigKey.values().forEach { key ->
                    writer.appendLine("${key.name}=")
                }
            }
        }
        return file
    }

    private fun initProperties(file: File): Properties {
        val properties = Properties()
        file.inputStream().use { configFile ->
            properties.load(configFile)
        }
        val missingProperties = ConfigKey.values().filter { key ->
            properties.getProperty(key.toString()).isNullOrBlank()
        }
        if (missingProperties.any()) {
            throw IllegalArgumentException("Missing $missingProperties in $CONFIG_FILE file")
        }
        return properties
    }

    operator fun get(key: ConfigKey): String =
        properties.getProperty(key.toString())

}
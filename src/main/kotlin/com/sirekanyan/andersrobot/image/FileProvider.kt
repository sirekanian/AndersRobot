package com.sirekanyan.andersrobot.image

import com.sirekanyan.andersrobot.extensions.getClosest
import java.io.File
import java.io.FilenameFilter

class FileProvider(extension: String) {

    private val fileRegex = Regex("\\d{3}.$extension")
    private val fileFilter = FilenameFilter { _, name -> name.matches(fileRegex) }
    private val files: Map<Int, File> =
        checkNotNull(File("data").listFiles(fileFilter))
            .associateBy({ it.nameWithoutExtension.toInt() }, { it })

    fun getFiles(): Map<Int, File> = files

    fun findFile(key: Int): File? = files[key] ?: files.getClosest(key)

}
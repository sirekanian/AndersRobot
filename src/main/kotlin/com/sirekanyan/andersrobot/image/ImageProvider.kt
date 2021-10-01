package com.sirekanyan.andersrobot.image

import com.sirekanyan.andersrobot.extensions.getClosest
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class ImageProvider(extension: String, width: Int, height: Int) {

    private val fileProvider = FileProvider(extension)
    private val images: Map<Int, BufferedImage> =
        fileProvider.getFiles()
            .mapValues { (_, file) -> ImageIO.read(file) }
            .onEach { (id, image) ->
                checkNotNull(image) { "Cannot read image $id.$extension" }
                check(image.width == width) { "Wrong image width for $id" }
                check(image.height == height) { "Wrong image height for $id" }
            }

    fun findImage(key: Int): BufferedImage? = images[key] ?: images.getClosest(key)

}
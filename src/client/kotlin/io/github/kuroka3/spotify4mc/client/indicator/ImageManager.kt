package io.github.kuroka3.spotify4mc.client.indicator

import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import java.net.URL
import javax.imageio.ImageIO

object ImageManager {

    var albumArt: Identifier? = null; private set
    var dominantColor: Int = 0; private set
    private var lastId: String = ""

    fun loadImage(imgURL: URL, id: String, callback: () -> Unit = {}) {
        Thread {
            try {
                if (lastId == id) { callback(); return@Thread }

                val prevAlbumArt = albumArt
                val img = ImageIO.read(imgURL)
                dominantColor = ColorManager.findDominantColor(img)
                val nativeImg = NativeImage(img.width, img.height, false)
                for (x in 0 until img.width) {
                    for (y in 0 until img.height) {
                        nativeImg.setColor(x, y, ColorManager.fixColor(img.getRGB(x, y)))
                    }
                }

                albumArt = MinecraftClient.getInstance().textureManager.registerDynamicTexture("spotify4mc.album_artwork", NativeImageBackedTexture(nativeImg))
                lastId = id

                if (prevAlbumArt != null) MinecraftClient.getInstance().textureManager.destroyTexture(prevAlbumArt)

                callback()
                return@Thread
            } catch (e: Exception) {
                e.printStackTrace()
                return@Thread
            }
        }.start()
    }
}
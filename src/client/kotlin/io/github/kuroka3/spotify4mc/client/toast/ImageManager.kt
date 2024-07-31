package io.github.kuroka3.spotify4mc.client.toast

import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import java.net.URL
import javax.imageio.ImageIO

object ImageManager {

    var trackJacket: Identifier? = null; private set
    private var lastId: String = ""

    fun loadImage(imgURL: URL, id: String, callback: () -> Unit = {}) {
        Thread {
            try {
                if (lastId == id) { callback(); return@Thread }

                val prevTrackJacket = trackJacket
                val img = ImageIO.read(imgURL)
                val nativeImg = NativeImage(img.width, img.height, false)
                for (x in 0 until img.width) {
                    for (y in 0 until img.height) {
                        nativeImg.setColor(x, y, fixColor(img.getRGB(x, y)))
                    }
                }

                trackJacket = MinecraftClient.getInstance().textureManager.registerDynamicTexture("spotify4mc.track_jacket", NativeImageBackedTexture(nativeImg))
                lastId = id

                if (prevTrackJacket != null) MinecraftClient.getInstance().textureManager.destroyTexture(prevTrackJacket)

                callback()
                return@Thread
            } catch (e: Exception) {
                e.printStackTrace()
                return@Thread
            }
        }.start()
    }

    private fun fixColor(color: Int): Int {
        val alpha = (color shr 24) and 0xFF
        val red = (color shr 16) and 0xFF
        val green = (color shr 8) and 0xFF
        val blue = color and 0xFF

        // 색상 채널 순서를 조정
        return (alpha shl 24) or (blue shl 16) or (green shl 8) or red
    }
}
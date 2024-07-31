package io.github.kuroka3.spotify4mc.client.toast

import io.github.kuroka3.spotify4mc.client.api.classes.structures.SpotifyTrackState
import io.github.kuroka3.spotify4mc.client.api.classes.structures.SpotifyTrack
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.text.Text
import java.net.URI
import kotlin.math.roundToInt

class SpotifyHudOverlay : HudRenderCallback {
    companion object {
        val instance = SpotifyHudOverlay()

        fun indicateState(state: SpotifyTrackState) {
            val track = state.item
            val img = track.album.images[0]
            instance.track = track
            ImageManager.loadImage(URI(img.url).toURL(), track.id)
            instance.currentMs = state.progressMs
            instance.isPlaying = state.isPlaying
        }
    }

    private var track: SpotifyTrack? = null
    private var currentMs: Int = 0
    private var isPlaying: Boolean = false

    private var lastUpdate: Long = System.currentTimeMillis()

    override fun onHudRender(drawContext: DrawContext, tickCounter: RenderTickCounter) {
        if (ImageManager.trackJacket == null || track == null) { lastUpdate = System.currentTimeMillis(); return }

        if (isPlaying) currentMs += (System.currentTimeMillis() - lastUpdate).toInt()
        lastUpdate = System.currentTimeMillis()

        val client = MinecraftClient.getInstance()
        val windowWidth = client.window.scaledWidth
        val windowHeight = client.window.scaledHeight

        val jacketSize = Pair(32, 32)
        val width = SpotifyConfig.instance.displayWidth
        val height = 50

        val x = windowWidth - width
        val y = windowHeight - height

        drawBackground(drawContext, x, y, windowWidth, windowHeight)
        drawJacket(drawContext, x+9, y+6, jacketSize)
        drawProgressBar(drawContext, x+9, windowHeight-8, currentMs.toFloat()/track!!.durationMs.toFloat())
        drawTitle(drawContext, client.textRenderer, x+9+jacketSize.first+10, y+6, Text.literal(track!!.name), Text.literal(track!!.artists.joinToString(", ") { it.name } ))
    }

    private fun drawBackground(context: DrawContext, x: Int, y: Int, windowWidth: Int, windowHeight: Int) {
        context.fill(x, y, windowWidth, windowHeight, 0x55000000)
    }

    private fun drawJacket(context: DrawContext, x: Int, y: Int, jacketSize: Pair<Int, Int>) {
        context.drawTexture(ImageManager.trackJacket, x, y, 0f, 0f, jacketSize.first, jacketSize.second, jacketSize.first, jacketSize.second)
    }

    private fun drawProgressBar(context: DrawContext, x: Int, y: Int, value: Float) {
        val totalPixels = SpotifyConfig.instance.displayWidth - 18
        val emptyColor = (0xff909090).toInt()
        val filledColor = (0xffffffff).toInt()

        val filledPixels = (value*totalPixels.toFloat()).roundToInt()
        val emptyPixels = totalPixels - filledPixels

        if (filledPixels == 0) {
            context.fill(x, y, x+totalPixels, y+2, emptyColor)
        } else if (emptyPixels == 0) {
            context.fill(x, y, x+totalPixels, y+2, filledColor)
        } else {
            context.fill(x, y, x+filledPixels, y+2, filledColor)
            context.fill(x+filledPixels, y, x+totalPixels, y+2, emptyColor)
        }
    }

    private fun drawTitle(context: DrawContext, renderer: TextRenderer, x: Int, y: Int, name: Text, artist: Text) {
        context.drawText(renderer, name, x, y, 0x00ffffff, false)
        context.drawText(renderer, artist, x, y+13, 0x00ffffff, false)
    }
}
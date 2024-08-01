package io.github.kuroka3.spotify4mc.client.indicator

import io.github.kuroka3.spotify4mc.client.api.classes.structures.SpotifyTrackState
import io.github.kuroka3.spotify4mc.client.api.classes.structures.SpotifyTrack
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.text.OrderedText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.net.URI
import kotlin.math.roundToInt

class SpotifyHudOverlay : HudRenderCallback {
    private val SPOTIFY_LOGO = Identifier.of("spotify4mc", "textures/spotify/logo.png")
    private val SPOTIFY_ICON = Identifier.of("spotify4mc", "textures/spotify/icon.png")

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
    var isPlaying: Boolean = false

    private var lastUpdate: Long = System.currentTimeMillis()

    override fun onHudRender(drawContext: DrawContext, tickCounter: RenderTickCounter) {
        if (ImageManager.albumArt == null || track == null) { lastUpdate = System.currentTimeMillis(); return }

        if (isPlaying) currentMs += (System.currentTimeMillis() - lastUpdate).toInt()
        if (currentMs >= track!!.durationMs) currentMs = track!!.durationMs
        lastUpdate = System.currentTimeMillis()

        val client = MinecraftClient.getInstance()
        val windowWidth = client.window.scaledWidth
        val windowHeight = client.window.scaledHeight

        val albumArtSize = Pair(32, 32)
        val width = SpotifyConfig.instance.displayWidth
        val height = 50

        val x = windowWidth - width
        val y = windowHeight - height

        val trackName = Text.literal(track!!.name)
        val trackArtist = Text.literal(track!!.artists.joinToString(", ") { it.name } )

        val trackNameSize = client.textRenderer.getWidth(trackName)
        val trackArtistSize = client.textRenderer.getWidth(trackArtist)
        var titleSize = if (trackNameSize > trackArtistSize) trackNameSize else trackArtistSize

        drawBackground(drawContext, x, y, windowWidth, windowHeight)
        drawAlbumArt(drawContext, x+9, y+6, albumArtSize)
        if ((width-18-albumArtSize.first) >= titleSize+5) drawTitle(drawContext, client.textRenderer, x+9+albumArtSize.first+10, y+6, trackName, trackArtist)
        else if (width >= 60) {
            val trackNameWrapped = client.textRenderer.wrapLines(trackName, width-18-albumArtSize.first-5)
            val trackArtistWrapped = client.textRenderer.wrapLines(trackArtist, width-18-albumArtSize.first-5)
            drawTitle(drawContext, client.textRenderer, x+9+albumArtSize.first+10, y+6, trackNameWrapped[0], trackArtistWrapped[0])

            val wrappedTrackNameSize = client.textRenderer.getWidth(trackNameWrapped[0])
            val wrappedTrackArtistSize = client.textRenderer.getWidth(trackArtistWrapped[0])

            titleSize = if (wrappedTrackNameSize > wrappedTrackArtistSize) wrappedTrackNameSize else wrappedTrackArtistSize
        }
        else titleSize = 0
        if ((width-18-albumArtSize.first-64) >= titleSize+5) drawSpotifyLogo(drawContext, windowWidth-9-64,y+6)
        else if ((width-18-albumArtSize.first-16) >= titleSize+5) drawSpotifyIcon(drawContext, windowWidth-9-16,y+6)
        drawProgressBar(drawContext, x+9, windowHeight-8, currentMs.toFloat()/track!!.durationMs.toFloat())
    }

    private fun drawBackground(context: DrawContext, x: Int, y: Int, windowWidth: Int, windowHeight: Int) {
        context.fill(x, y, windowWidth, windowHeight, ColorManager.addAlphaToHexColor(ImageManager.dominantColor, 85))
    }

    private fun drawAlbumArt(context: DrawContext, x: Int, y: Int, albumArtSize: Pair<Int, Int>) {
        context.drawTexture(ImageManager.albumArt, x, y, 0f, 0f, albumArtSize.first, albumArtSize.second, albumArtSize.first, albumArtSize.second)
    }

    private fun drawSpotifyLogo(context: DrawContext, x: Int, y: Int) {
        context.drawTexture(SPOTIFY_LOGO, x, y, 0f, 0f, 64, 16, 64, 16)
    }

    private fun drawSpotifyIcon(context: DrawContext, x: Int, y: Int) {
        context.drawTexture(SPOTIFY_ICON, x, y, 0f, 0f, 16, 16, 16, 16)
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
        context.drawText(renderer, name, x, y, (0xffffffff).toInt(), false)
        context.drawText(renderer, artist, x, y+13, (0xffffffff).toInt(), false)
    }

    private fun drawTitle(context: DrawContext, renderer: TextRenderer, x: Int, y: Int, name: OrderedText, artist: OrderedText) {
        context.drawText(renderer, name, x, y, (0xffffffff).toInt(), false)
        context.drawText(renderer, artist, x, y+13, (0xffffffff).toInt(), false)
    }
}
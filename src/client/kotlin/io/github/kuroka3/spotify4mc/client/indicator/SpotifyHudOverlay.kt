package io.github.kuroka3.spotify4mc.client.indicator

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

        fun indicateState() {
            val state = IndicateManager.currentState
            val track = state.item
            val img = track.album.images[0]
            instance.track = track
            if (SpotifyConfig.instance.showAlbumArt) ImageManager.loadImage(URI(img.url).toURL(), track.id)
            instance.currentMs = state.progressMs
            instance.isPlaying = state.isPlaying
        }
    }

    private var track: SpotifyTrack? = null
    private var currentMs: Int = 0
    var isPlaying: Boolean = false

    private var lastUpdate: Long = System.currentTimeMillis()

    override fun onHudRender(drawContext: DrawContext, tickCounter: RenderTickCounter) {
        if ((SpotifyConfig.instance.showAlbumArt && ImageManager.albumArt == null) || track == null) { lastUpdate = System.currentTimeMillis(); return }

        if (isPlaying) currentMs += (System.currentTimeMillis() - lastUpdate).toInt()
        if (currentMs >= track!!.durationMs) currentMs = track!!.durationMs
        lastUpdate = System.currentTimeMillis()

        val client = MinecraftClient.getInstance()
        val windowWidth = client.window.scaledWidth
        val windowHeight = client.window.scaledHeight

        val albumArtSize = if (SpotifyConfig.instance.showAlbumArt) Pair(32, 32) else Pair(-9, 0)
        val width = SpotifyConfig.instance.displayWidth
        val height = 50

        val x = windowWidth - width
        val y = windowHeight - height

        val hudSquare = HudSquare(x, y, windowWidth, windowHeight)

        val trackName = Text.literal(track!!.name)
        val trackArtist = Text.literal(track!!.artists.joinToString(", ") { it.name } )

        val titleSize = if (SpotifyConfig.instance.showTrackInfo) calculateTitle(client.textRenderer, trackName, trackArtist, width-(9+albumArtSize.first+10+5)).first else -10

        drawBackground(drawContext, hudSquare)
        drawAlbumArt(drawContext, hudSquare, albumArtSize, Margin(9, 6, 0, 0))
        drawTitle(drawContext, client.textRenderer, hudSquare, Margin(9+albumArtSize.first+10, 6, 5, 0), trackName, trackArtist)
        drawProgressBar(drawContext, hudSquare, Margin(9, 0, 9, 8), currentMs.toFloat()/track!!.durationMs.toFloat())
        drawSpotifyLogo(drawContext, hudSquare, Margin(9+albumArtSize.first+10+titleSize+5, 6, 9, 0))
    }

    private fun drawBackground(context: DrawContext, hudSquare: HudSquare) {
        context.fill(hudSquare.x1, hudSquare.y1, hudSquare.x2, hudSquare.y2, ColorManager.addAlphaToHexColor(ImageManager.dominantColor, (SpotifyConfig.instance.backgroundOpacity*255).roundToInt()))
    }

    private fun drawAlbumArt(context: DrawContext, hudSquare: HudSquare, albumArtSize: Pair<Int, Int>, margin: Margin) {
        if (SpotifyConfig.instance.showAlbumArt) context.drawTexture(ImageManager.albumArt, hudSquare.x1+margin.left, hudSquare.y1+margin.top, 0f, 0f, albumArtSize.first, albumArtSize.second, albumArtSize.first, albumArtSize.second)
    }

    private fun drawSpotifyLogo(context: DrawContext, hudSquare: HudSquare, margin: Margin) {
        val width = hudSquare.x2 - hudSquare.x1

        if ((width-margin.left-margin.right) >= 69) context.drawTexture(SPOTIFY_LOGO, hudSquare.x2-margin.right-64, hudSquare.y1+margin.top, 0f, 0f, 64, 16, 64, 16)
        else if ((width-margin.left-margin.right) >= 21) context.drawTexture(SPOTIFY_ICON, hudSquare.x2-margin.right-16, hudSquare.y1+margin.top, 0f, 0f, 16, 16, 16, 16)
    }

    private fun drawProgressBar(context: DrawContext, hudSquare: HudSquare, margin: Margin, value: Float) {
        val totalPixels = SpotifyConfig.instance.displayWidth - margin.left - margin.right
        val x = hudSquare.x1 + margin.left
        val y = hudSquare.y2 - margin.bottom

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

    private fun drawTitle(context: DrawContext, renderer: TextRenderer, hudSquare: HudSquare, margin: Margin, name: Text, artist: Text) {
        if (!SpotifyConfig.instance.showTrackInfo) return

        val width = hudSquare.x2 - hudSquare.x1
        val title = calculateTitle(renderer, name, artist, width-margin.left-margin.right)

        context.drawText(renderer, title.second.first, hudSquare.x1+margin.left, hudSquare.y1+margin.top, (0xffffffff).toInt(), true)
        context.drawText(renderer, title.second.second, hudSquare.x1+margin.left, hudSquare.y1+margin.top+13, (0xffffffff).toInt(), true)
    }

    private fun calculateTitle(renderer: TextRenderer, name: Text, artist: Text, sizeLimit: Int): Pair<Int, Pair<OrderedText, OrderedText>> {
        val nameWrapped = renderer.wrapLines(name, sizeLimit)
        val artistWrapped = renderer.wrapLines(artist, sizeLimit)

        val nameSizeWrapped = renderer.getWidth(nameWrapped[0])
        val artistSizeWrapped = renderer.getWidth(artistWrapped[0])
        val titleSize = if(nameSizeWrapped > artistSizeWrapped) nameSizeWrapped else artistSizeWrapped

        return Pair(titleSize, Pair(nameWrapped[0], artistWrapped[0]))
    }

    private data class HudSquare(val x1: Int, val y1: Int, val x2: Int, val y2: Int)
    private data class Margin(val left: Int, val top: Int, val right: Int, val bottom: Int)
}
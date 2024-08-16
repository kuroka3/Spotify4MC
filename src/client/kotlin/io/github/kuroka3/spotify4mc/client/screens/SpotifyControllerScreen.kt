package io.github.kuroka3.spotify4mc.client.screens

import io.github.kuroka3.spotify4mc.client.controller.SpotifyController
import io.github.kuroka3.spotify4mc.client.indicator.*
import io.github.kuroka3.spotify4mc.client.indicator.ColorManager.addAlphaToHexColor
import io.github.kuroka3.spotify4mc.client.screens.widget.SquareWidget
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.IconWidget
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import kotlin.math.roundToInt

class SpotifyControllerScreen : Screen(Text.literal("Spotify Controller Screen")) {

    private val SPOTIFY_LOGO = Identifier.of("spotify4mc", "textures/spotify/logo.png")

    companion object {
        val instance: SpotifyControllerScreen = SpotifyControllerScreen()
    }

    private lateinit var start: Pair<Int, Int>
    private lateinit var size: Pair<Int, Int>

    private lateinit var backgroundWidget: SquareWidget

    private lateinit var albumArtWidget: IconWidget
    private lateinit var titleWidget: TextWidget
    private lateinit var artistWidget: TextWidget
    private lateinit var progressFilled: SquareWidget
    private lateinit var progressEmpty: SquareWidget
    private lateinit var spotifyLogoWidget: IconWidget

    private lateinit var previousButton: ButtonWidget
    private lateinit var pauseResumeButton: ButtonWidget
    private lateinit var nextButton: ButtonWidget

    override fun init() {
        val title = Text.literal(IndicateManager.currentState.item.name)
        val artist = Text.literal(IndicateManager.currentState.item.artists.joinToString(", ") { it.name } )
        val titleSize = TextManager.calculateTitle(client!!.textRenderer, title, artist, Int.MAX_VALUE)

        val indicatorWidth = (if (SpotifyConfig.instance.showAlbumArt) 32+10 else 0)+9+titleSize.first+64+5+9
        val indicatorHeight = 50
        start = Pair(width/2 - indicatorWidth/2, height/2 - indicatorHeight/2)
        size = Pair(indicatorWidth, indicatorHeight)

        initBackground(start)
        if (SpotifyConfig.instance.showAlbumArt) initAlbumArtWidget(start)
        initTextWidgets(start)
        initProgressWidget(start, IndicateManager.currentMs.toFloat()/IndicateManager.currentState.item.durationMs.toFloat())
        initSpotifyLogoWidget(start)

        initButtonWidgets()
    }

    fun refresh() {
        client!!.execute {
            client!!.setScreen(instance)
        }
    }

    fun refreshProgressOnly() {
        if (::progressEmpty.isInitialized && ::progressFilled.isInitialized) initProgressWidget(start, IndicateManager.currentMs.toFloat()/IndicateManager.currentState.item.durationMs.toFloat(), refresh = true)
    }

    private fun initBackground(start: Pair<Int, Int>) {
        backgroundWidget = SquareWidget(start.first, start.second, size.first, size.second, ImageManager.dominantColor.addAlphaToHexColor((SpotifyConfig.instance.backgroundOpacity*255).roundToInt()))

        addDrawableChild(backgroundWidget)
    }

    private fun initAlbumArtWidget(start: Pair<Int, Int>) {
        albumArtWidget = IconWidget.create(32, 32, ImageManager.albumArt, 32, 32)
        albumArtWidget.x = start.first + 9
        albumArtWidget.y = start.second + 6

        addDrawableChild(albumArtWidget)
    }

    private fun initProgressWidget(start: Pair<Int, Int>, value: Float, refresh: Boolean = false) {
        val totalPixels = size.first - 18
        val x = start.first + 9
        val y = start.second + size.second - 8

        val emptyColor = (0xff909090).toInt()
        val filledColor = (0xffffffff).toInt()

        val filledPixels = (value*totalPixels.toFloat()).roundToInt()
        val emptyPixels = totalPixels - filledPixels

        if (refresh) {
            when(0) {
                filledPixels -> {
                    progressFilled.width = 0
                    progressEmpty.width = totalPixels
                }
                emptyPixels -> {
                    progressFilled.width = totalPixels
                    progressEmpty.width = 0
                }
                else -> {
                    progressFilled.width = filledPixels
                    progressEmpty.x = x+filledPixels
                    progressEmpty.width = totalPixels-filledPixels
                }
            }
        } else {
            when(0) {
                filledPixels -> {
                    progressFilled = SquareWidget(x, y, 0, 2, emptyColor)
                    progressEmpty = SquareWidget(x, y, totalPixels, 2, emptyColor)
                }
                emptyPixels -> {
                    progressFilled = SquareWidget(x, y, totalPixels, 2, filledColor)
                    progressEmpty = SquareWidget(x, y, 0, 2, filledColor)
                }
                else -> {
                    progressFilled = SquareWidget(x, y, filledPixels, 2, filledColor)
                    progressEmpty = SquareWidget(x+filledPixels, y, totalPixels-filledPixels, 2, emptyColor)
                }
            }
        }

        addDrawableChild(progressFilled)
        addDrawableChild(progressEmpty)
    }

    private fun initTextWidgets(start: Pair<Int, Int>) {
        val renderer = MinecraftClient.getInstance().textRenderer

        val title = Text.literal(IndicateManager.currentState.item.name)
        val artist = Text.literal(IndicateManager.currentState.item.artists.joinToString(", ") { it.name } )
        val titleSize = renderer.getWidth(title)
        val artistSize = renderer.getWidth(artist)

        titleWidget = TextWidget(start.first+9+(if (SpotifyConfig.instance.showAlbumArt) 32+10 else 0), start.second+6, titleSize, 10, Text.literal(IndicateManager.currentState.item.name), renderer)
        artistWidget = TextWidget(start.first+9+(if (SpotifyConfig.instance.showAlbumArt) 32+10 else 0), start.second+19, artistSize, 10, Text.literal(IndicateManager.currentState.item.artists.joinToString(", ") { it.name } ), renderer)

        addDrawableChild(titleWidget)
        addDrawableChild(artistWidget)
    }

    private fun initSpotifyLogoWidget(start: Pair<Int, Int>) {
        val x = start.first + size.first - 9 - 64
        val y = start.second + 6

        spotifyLogoWidget = IconWidget.create(64, 16, SPOTIFY_LOGO, 64, 16)
        spotifyLogoWidget.x = x
        spotifyLogoWidget.y = y

        addDrawableChild(spotifyLogoWidget)
    }

    private fun initButtonWidgets() {
        previousButton = ButtonWidget.builder(
            Text.literal("|<"),
            this::previousButtonHandle)
            .dimensions(width/2 - 160, height/2 + 26, 100, 20)
            .tooltip(Tooltip.of(Text.literal("Previous")))
            .build()

        pauseResumeButton = ButtonWidget.builder(
            Text.literal(if (IndicateManager.isPlaying) "⏸" else "⏵"),
            this::pauseResumeButtonHandle)
            .dimensions(width/2 - 50, height/2 + 26, 100, 20)
            .tooltip(Tooltip.of(Text.literal(if (IndicateManager.isPlaying) "Pause" else "Resume")))
            .build()

        nextButton = ButtonWidget.builder(
            Text.literal(">|"),
            this::nextButtonHandle)
            .dimensions(width/2 + 60, height/2 + 26, 100, 20)
            .tooltip(Tooltip.of(Text.literal("Next")))
            .build()

        addDrawableChild(previousButton)
        addDrawableChild(pauseResumeButton)
        addDrawableChild(nextButton)
    }

    private fun previousButtonHandle(buttonWidget: ButtonWidget) {
        SpotifyController.skipPrev()
    }

    private fun pauseResumeButtonHandle(buttonWidget: ButtonWidget) {
        SpotifyController.togglePause()
        pauseResumeButton.message = Text.literal(if (IndicateManager.isPlaying) "⏸" else "⏵")
        pauseResumeButton.tooltip = Tooltip.of(Text.literal(if (IndicateManager.isPlaying) "Pause" else "Resume"))
    }

    private fun nextButtonHandle(buttonWidget: ButtonWidget) {
        SpotifyController.skipNext()
    }
}
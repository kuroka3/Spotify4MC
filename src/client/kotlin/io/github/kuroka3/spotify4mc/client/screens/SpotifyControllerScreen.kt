package io.github.kuroka3.spotify4mc.client.screens

import io.github.kuroka3.spotify4mc.client.controller.SpotifyController
import io.github.kuroka3.spotify4mc.client.indicator.ImageManager
import io.github.kuroka3.spotify4mc.client.indicator.IndicateManager
import io.github.kuroka3.spotify4mc.client.indicator.SpotifyHudOverlay
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.IconWidget
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.text.Text

class SpotifyControllerScreen : Screen(Text.literal("Spotify Controller Screen")) {

    companion object {
        val instance: SpotifyControllerScreen = SpotifyControllerScreen()
    }

    private lateinit var albumArtWidget: IconWidget
    private lateinit var titleWidget: TextWidget
    private lateinit var artistWidget: TextWidget

    private lateinit var previousButton: ButtonWidget
    private lateinit var pauseResumeButton: ButtonWidget
    private lateinit var nextButton: ButtonWidget

    override fun init() {
        if (SpotifyConfig.instance.showAlbumArt) initAlbumArtWidget()
        initTextWidgets()
        initButtonWidgets()
    }

    fun refreshTrack() {
        if (SpotifyConfig.instance.showAlbumArt) {
            remove(albumArtWidget)
            initAlbumArtWidget()
        }

        remove(titleWidget)
        remove(artistWidget)

        initTextWidgets()
    }

    private fun initAlbumArtWidget() {
        albumArtWidget = IconWidget.create(64, 64, ImageManager.albumArt, 64, 64)
        albumArtWidget.x = width/2 - 32
        albumArtWidget.y = height/2 - 67

        addDrawableChild(albumArtWidget)
    }

    private fun initTextWidgets() {
        val renderer = MinecraftClient.getInstance().textRenderer

        val title = Text.literal(IndicateManager.currentState.item.name)
        val artist = Text.literal(IndicateManager.currentState.item.artists.joinToString(", ") { it.name } )
        val titleSize = renderer.getWidth(title)
        val artistSize = renderer.getWidth(artist)

        titleWidget = TextWidget((width/2) - (titleSize/2), height/2, titleSize, 10, Text.literal(IndicateManager.currentState.item.name), renderer)
        artistWidget = TextWidget((width/2) - (artistSize/2), height/2 + 13, artistSize, 10, Text.literal(IndicateManager.currentState.item.artists.joinToString(", ") { it.name } ), renderer)

        addDrawableChild(titleWidget)
        addDrawableChild(artistWidget)
    }

    private fun initButtonWidgets() {
        previousButton = ButtonWidget.builder(
            Text.literal("|<"),
            this::previousButtonHandle)
            .dimensions(width/2 - 160, height/2 + 26, 100, 20)
            .tooltip(Tooltip.of(Text.literal("Previous")))
            .build()

        pauseResumeButton = ButtonWidget.builder(
            Text.literal(if (SpotifyHudOverlay.instance.isPlaying) "⏸" else "⏵"),
            this::pauseResumeButtonHandle)
            .dimensions(width/2 - 50, height/2 + 26, 100, 20)
            .tooltip(Tooltip.of(Text.literal(if (SpotifyHudOverlay.instance.isPlaying) "Pause" else "Resume")))
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
        pauseResumeButton.message = Text.literal(if (SpotifyHudOverlay.instance.isPlaying) "⏸" else "⏵")
        pauseResumeButton.tooltip = Tooltip.of(Text.literal(if (SpotifyHudOverlay.instance.isPlaying) "Pause" else "Resume"))
    }

    private fun nextButtonHandle(buttonWidget: ButtonWidget) {
        SpotifyController.skipNext()
    }
}
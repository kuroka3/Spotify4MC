package io.github.kuroka3.spotify4mc.client.screens.widget

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.navigation.GuiNavigation
import net.minecraft.client.gui.navigation.GuiNavigationPath
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.sound.SoundManager
import net.minecraft.screen.ScreenTexts

class SquareWidget(x: Int, y: Int, width: Int, height: Int, private val color: Int) : ClickableWidget(x, y, width, height, ScreenTexts.EMPTY) {

    override fun appendClickableNarrations(builder: NarrationMessageBuilder) {
    }

    override fun playDownSound(soundManager: SoundManager) {
    }

    override fun isNarratable(): Boolean = false

    override fun getNavigationPath(navigation: GuiNavigation): GuiNavigationPath? {
        return null
    }

    override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        context.fill(this.x, this.y, this.x + this.width, this.y + this.height, this.color)
    }
}
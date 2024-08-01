package io.github.kuroka3.spotify4mc.client.indicator

import io.github.kuroka3.spotify4mc.client.api.classes.structures.SpotifyTrack
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.toast.Toast
import net.minecraft.client.toast.ToastManager
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class SpotifyToast(val track: SpotifyTrack, val displayTime: Long = 5000L) : Toast {

    private val TEXTURE = Identifier.ofVanilla("toast/advancement")
    private val NOW_PLAYING = Text.literal("♫ Now Playing")

    override fun draw(context: DrawContext, manager: ToastManager, startTime: Long): Toast.Visibility {
        drawBackground(context)
        ImageManager.albumArt?.let { drawTexture(it, context) }
        drawText(context, Text.literal(track.name), Text.literal(track.artists.joinToString(", ") { it.name }), MinecraftClient.getInstance().textRenderer, startTime)

        return if (startTime >= displayTime) {
            Toast.Visibility.HIDE
        } else {
            Toast.Visibility.SHOW
        }
    }

    private fun drawBackground(context: DrawContext) {
        context.drawGuiTexture(TEXTURE, 0, 0, this.width, this.height)
    }

    private fun drawTexture(texture: Identifier, context: DrawContext) {
        context.drawTexture(texture, 8, 8, 0F, 0F, 16, 16, 16, 16)
    }

    private fun drawText(context: DrawContext, title: Text, desc: Text, renderer: TextRenderer, startTime: Long) {
        if (startTime < 1500L) {
            context.drawText(renderer, NOW_PLAYING, 30, 11, 0x00ffff55, false)
        } else if (startTime < 1756L) {
            val elapsed = (startTime - 1500L).toInt()
            context.drawText(renderer, NOW_PLAYING, 30, 11, ColorManager.addAlphaToHexColor(0x00ffff55, 255-elapsed), false)
            context.drawText(renderer, title, 30, 7, ColorManager.addAlphaToHexColor(0x00ffffff, elapsed), false)
            context.drawText(renderer, desc, 30, 16, ColorManager.addAlphaToHexColor(0x00ffffff, elapsed), false)
        } else {
            context.drawText(renderer, title, 30, 7, 0x00ffffff, false)
            context.drawText(renderer, desc, 30, 16, 0x00ffffff, false)
        }
    }
}
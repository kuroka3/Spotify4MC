package io.github.kuroka3.spotify4mc.client.controller

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object KeyInputHandler {
    const val KEY_CATEGORY = "spotify4mc.key.category.spotify_player_control"
    const val KEY_NEXT = "spotify4mc.key.next"
    const val KEY_PREV = "spotify4mc.key.prev"
    const val KEY_PAUSE_RESUME = "spotify4mc.key.pause_resume"

    lateinit var keyNext: KeyBinding
    lateinit var keyPrev: KeyBinding
    lateinit var keyPauseResume: KeyBinding

    private fun registerKeyHandler() {
        ClientTickEvents.END_CLIENT_TICK.register {
            if(keyNext.wasPressed()) SpotifyController.skipNext()
            if(keyPrev.wasPressed()) SpotifyController.skipPrev()
            if(keyPauseResume.wasPressed()) SpotifyController.togglePause()
        }
    }

    fun reqister() {
        keyNext = KeyBindingHelper.registerKeyBinding(KeyBinding(
            KEY_NEXT,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT,
            KEY_CATEGORY
        ))

        keyPrev = KeyBindingHelper.registerKeyBinding(KeyBinding(
            KEY_PREV,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT,
            KEY_CATEGORY
        ))

        keyPauseResume = KeyBindingHelper.registerKeyBinding(KeyBinding(
            KEY_PAUSE_RESUME,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            KEY_CATEGORY
        ))

        registerKeyHandler()
    }
}
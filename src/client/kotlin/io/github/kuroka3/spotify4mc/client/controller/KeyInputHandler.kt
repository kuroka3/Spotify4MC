package io.github.kuroka3.spotify4mc.client.controller

import io.github.kuroka3.spotify4mc.client.screens.SpotifyControllerScreen
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW

object KeyInputHandler {
    const val KEY_CATEGORY = "spotify4mc.key.category.spotify4mc"
    const val KEY_NEXT = "spotify4mc.key.next"
    const val KEY_PREV = "spotify4mc.key.prev"
    const val KEY_PAUSE_RESUME = "spotify4mc.key.pause_resume"
    const val KEY_OPEN_CONTROLLER = "spotify4mc.key.open_controller"

    lateinit var keyNext: KeyBinding
    lateinit var keyPrev: KeyBinding
    lateinit var keyPauseResume: KeyBinding
    lateinit var keyOpenController: KeyBinding

    private fun registerKeyHandler() {
        ClientTickEvents.END_CLIENT_TICK.register {
            if(keyNext.wasPressed()) SpotifyController.skipNext()
            if(keyPrev.wasPressed()) SpotifyController.skipPrev()
            if(keyPauseResume.wasPressed()) SpotifyController.togglePause()
            if(keyOpenController.wasPressed()) {
                if (SpotifyConfig.instance.token.accessToken == "token") MinecraftClient.getInstance().player?.sendMessage(Text.literal("Login First"))
                else MinecraftClient.getInstance().setScreen(SpotifyControllerScreen.instance)
            }
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

        keyOpenController = KeyBindingHelper.registerKeyBinding(KeyBinding(
            KEY_OPEN_CONTROLLER,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            KEY_CATEGORY
        ))

        registerKeyHandler()
    }
}
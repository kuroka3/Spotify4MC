package io.github.kuroka3.spotify4mc.client

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.kuroka3.spotify4mc.client.screens.SettingsScreen
import net.minecraft.client.gui.screen.Screen

class Spotify4mcModMenuApi : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory<Screen> { parent: Screen? ->
            SettingsScreen()
        }
    }
}
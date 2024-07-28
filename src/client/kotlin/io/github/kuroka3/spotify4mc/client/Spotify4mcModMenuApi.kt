package io.github.kuroka3.spotify4mc.client

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import net.minecraft.client.gui.screen.Screen

class Spotify4mcModMenuApi : ModMenuApi {

    var isSpotify: Boolean = true

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory<Screen> { parent: Screen? ->
            SpotifyConfig.instance.createGui(parent = parent)
        }
    }
}
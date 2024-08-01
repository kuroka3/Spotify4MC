package io.github.kuroka3.spotify4mc.client

import io.github.kuroka3.spotify4mc.client.indicator.SpotifyHudOverlay
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback

class Spotify4mcClient : ClientModInitializer {

    override fun onInitializeClient() {
        HudRenderCallback.EVENT.register(SpotifyHudOverlay.instance)
    }
}

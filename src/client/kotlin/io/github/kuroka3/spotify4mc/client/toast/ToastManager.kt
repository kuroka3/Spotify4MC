package io.github.kuroka3.spotify4mc.client.toast

import io.github.kuroka3.spotify4mc.client.api.classes.structures.SpotifyTrackState
import net.minecraft.client.MinecraftClient
import java.net.URI

object ToastManager {
    fun indicateState(state: SpotifyTrackState) {
        ImageManager.loadImage(URI(state.item.album.images[0].url).toURL(), state.item.id) {
            MinecraftClient.getInstance().toastManager.add(SpotifyToast(state.item))
        }
    }
}
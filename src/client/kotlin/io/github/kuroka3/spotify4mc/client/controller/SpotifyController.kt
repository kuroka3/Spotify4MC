package io.github.kuroka3.spotify4mc.client.controller

import io.github.kuroka3.spotify4mc.client.api.utils.HttpRequestManager
import io.github.kuroka3.spotify4mc.client.api.utils.TokenManager
import io.github.kuroka3.spotify4mc.client.indicator.SpotifyHudOverlay
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object SpotifyController {
    fun skipNext() {
        request("/me/player/next", "POST", "Skipped to Next")
    }

    fun skipPrev() {
        request("/me/player/previous", "POST", "Skipped to previous")
    }

    fun togglePause() {
        if (SpotifyHudOverlay.instance.isPlaying) {
            request("/me/player/pause", "PUT", "Paused")
            SpotifyHudOverlay.instance.isPlaying = false
        }
        else {
            request("/me/player/play", "PUT", "Resumed")
            SpotifyHudOverlay.instance.isPlaying = true
        }
    }

    private fun request(endpoint: String, method: String, success: String) {
        Thread {
            if (SpotifyConfig.instance.token.isExpired) TokenManager.refreshToken()
            HttpRequestManager.request(endpoint, method) { response ->
                if (response.statusCode == 403 && response.body!!.contains("premium")) {
                    MinecraftClient.getInstance().player?.sendMessage(Text.literal("Premium Required"))
                } else if (response.statusCode in 400..499) {
                    MinecraftClient.getInstance().player?.sendMessage(Text.literal("Something Went Wrong"))
                } else {
                    MinecraftClient.getInstance().player?.sendMessage(Text.literal(success))
                }
            }
        }.start()
    }
}
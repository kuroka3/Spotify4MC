package io.github.kuroka3.spotify4mc.client.indicator

import io.github.kuroka3.spotify4mc.client.api.classes.structures.SpotifyTrackState
import io.github.kuroka3.spotify4mc.client.api.utils.HttpRequestManager
import io.github.kuroka3.spotify4mc.client.api.utils.TokenManager
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import java.util.*

object IndicateManager {
    lateinit var currentState: SpotifyTrackState

    private lateinit var timer: Timer

    fun startIndicate() {
        timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                Thread {
                    val token = SpotifyConfig.instance.token
                    if (token.accessToken == "token") { MinecraftClient.getInstance().player?.sendMessage(Text.literal("Login First")); stopIndicate() }
                    if (token.isExpired) TokenManager.refreshToken()
                    HttpRequestManager.request("/me/player", "GET") {
                        try {
                            currentState = SpotifyTrackState.fromJson(it.body!!)
                            SpotifyHudOverlay.indicateState()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            stopIndicate()
                        }
                    }
                }.start()
            }
        }
        timer.schedule(task, 0L, SpotifyConfig.instance.apiRequestInterval*50L)
    }

    fun stopIndicate() {
        timer.cancel()
    }
}
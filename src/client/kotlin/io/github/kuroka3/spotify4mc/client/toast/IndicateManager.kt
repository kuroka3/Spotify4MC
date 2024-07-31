package io.github.kuroka3.spotify4mc.client.toast

import io.github.kuroka3.spotify4mc.client.api.classes.structures.SpotifyTrackState
import io.github.kuroka3.spotify4mc.client.api.utils.HttpRequestManager
import io.github.kuroka3.spotify4mc.client.api.utils.TokenManager
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import java.util.*

object IndicateManager {
    private lateinit var timer: Timer

    fun startIndicate() {
        timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                Thread {
                    val token = SpotifyConfig.instance.token
                    if (token.isExpired) TokenManager.refreshToken()
                    HttpRequestManager.request("/me/player", "GET") {
                        try {
                            val state = SpotifyTrackState.fromJson(it.body!!)
                            SpotifyHudOverlay.indicateState(state)
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
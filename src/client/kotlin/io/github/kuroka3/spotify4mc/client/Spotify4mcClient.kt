package io.github.kuroka3.spotify4mc.client

import io.github.kuroka3.spotify4mc.client.api.classes.structures.SpotifyTrack
import io.github.kuroka3.spotify4mc.client.api.utils.HttpRequestManager
import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager
import net.fabricmc.api.ClientModInitializer

class Spotify4mcClient : ClientModInitializer {

    override fun onInitializeClient() {
        HttpRequestManager.request("/tracks/24m0QkRKCICSr6Ma0i0eva", "GET") { response ->
            println(response.statusCode)
            println(JsonManager.gson.fromJson(response.body, SpotifyTrack::class.java).name)
        }
    }
}

package io.github.kuroka3.spotify4mc.client.api.classes.structures

import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager

data class SpotifyNarrator(
    val name: String
) {
    companion object {
        fun fromJson(json: String): SpotifyNarrator {
            return JsonManager.gson.fromJson(json, SpotifyNarrator::class.java)
        }
    }
}
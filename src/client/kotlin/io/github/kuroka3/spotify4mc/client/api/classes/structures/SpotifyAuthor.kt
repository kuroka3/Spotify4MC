package io.github.kuroka3.spotify4mc.client.api.classes.structures

import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager

data class SpotifyAuthor(
    val name: String
) {
    companion object {
        fun fromJson(json: String): SpotifyAuthor {
            return JsonManager.gson.fromJson(json, SpotifyAuthor::class.java)
        }
    }
}
package io.github.kuroka3.spotify4mc.client.api.classes.structures

import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager

data class SpotifyFollowers(
    val href: String?,
    val total: Int
) {
    companion object {
        fun fromJson(json: String): SpotifyFollowers {
            return JsonManager.gson.fromJson(json, SpotifyFollowers::class.java)
        }
    }
}
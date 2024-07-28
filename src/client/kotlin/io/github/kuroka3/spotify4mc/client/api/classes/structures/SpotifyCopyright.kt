package io.github.kuroka3.spotify4mc.client.api.classes.structures

import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager

data class SpotifyCopyright(
    val text: String,
    val type: String
) {
    companion object {
        fun fromJson(json: String): SpotifyCopyright {
            return JsonManager.gson.fromJson(json, SpotifyCopyright::class.java)
        }
    }
}
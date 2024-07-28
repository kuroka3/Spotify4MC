package io.github.kuroka3.spotify4mc.client.api.classes

import java.util.Base64

data class SpotifyApplication(
    val id: String,
    val secret: String
) {
    val authorization: String
        get() = "Basic ${Base64.getEncoder().encodeToString("${id}:${secret}".toByteArray())}"
}
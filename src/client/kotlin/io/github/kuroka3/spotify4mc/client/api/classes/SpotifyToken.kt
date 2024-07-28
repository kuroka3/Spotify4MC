package io.github.kuroka3.spotify4mc.client.api.classes

data class SpotifyToken(
    val accessToken: String,
    val tokenType: String,
    val scope: String,
    val expiresIn: Int,
    val refreshToken: String,
    val grantedAt: Long = System.currentTimeMillis()
) {
    val isExpired: Boolean
        get() = System.currentTimeMillis() > (expiresIn*1000L) + grantedAt

    override fun toString() = "$tokenType $accessToken"
}
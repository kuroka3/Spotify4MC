package io.github.kuroka3.spotify4mc.client.api.utils

import io.github.kuroka3.spotify4mc.client.api.classes.SpotifyApplication
import io.github.kuroka3.spotify4mc.client.api.classes.SpotifyToken
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import net.minecraft.util.Util
import java.net.HttpURLConnection
import java.net.URI

object TokenManager {
    fun login() {
        WebAuthorizer.run()
        Util.getOperatingSystem().open(URI("http://localhost:${SpotifyConfig.instance.authServerPort}/login"))
    }

    fun refreshToken(token: SpotifyToken): Boolean {
        val refreshToken = token.refreshToken
        val url = URI("https://accounts.spotify.com/api/token").toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        connection.setRequestProperty("Authorization", SpotifyApplication(SpotifyConfig.instance.clientID, SpotifyConfig.instance.clientSecret).authorization)

        val body = "grant_type=refresh_token&refresh_token=$refreshToken"
        connection.doOutput = true
        connection.outputStream.use { it.write(body.toByteArray()) }

        val responseCode = connection.responseCode
        return if (responseCode == HttpURLConnection.HTTP_OK) {
            val responseBody = connection.inputStream.bufferedReader().use { it.readText() }
            val refreshedToken = JsonManager.gson.fromJson(responseBody, SpotifyToken::class.java)
            SpotifyConfig.instance.token = refreshedToken
            SpotifyConfig.instance.save()
            true
        } else {
            false
        }
    }
}
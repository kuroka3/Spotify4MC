package io.github.kuroka3.spotify4mc.client.api.utils

import io.github.kuroka3.spotify4mc.client.api.classes.HttpResponse
import io.github.kuroka3.spotify4mc.client.api.classes.SpotifyToken
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import java.net.HttpURLConnection
import java.net.URI

object HttpRequestManager {
    const val SPOTIFY_API_BASE_URL = "https://api.spotify.com/v1"

    fun request(endpoint: String, method: String, params: String = "", body: ByteArray = ByteArray(0), callback: (response: HttpResponse) -> Unit = {}): Thread {
        val thread = Thread {
            val url = URI("$SPOTIFY_API_BASE_URL$endpoint?$params").toURL()
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = method
            connection.setRequestProperty("Authorization", SpotifyConfig.instance.token.toString()) //TODO Tokenmanager

            if (method != "GET") {
                connection.doOutput = true
                connection.outputStream.use { it.write(body) }
            }

            callback(HttpResponse(connection.responseCode, connection.inputStream.bufferedReader().readText()))
        }
        thread.start()
        return thread
    }
}
package io.github.kuroka3.spotify4mc.client.api.utils

import io.github.kuroka3.spotify4mc.client.api.classes.SpotifyApplication
import io.github.kuroka3.spotify4mc.client.api.classes.SpotifyToken
import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import io.javalin.Javalin
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import java.net.HttpURLConnection
import java.net.URI
import java.net.URLEncoder
import java.util.*

object WebAuthorizer {

    private lateinit var app: Javalin
    private val redirect = "http://localhost:${SpotifyConfig.instance.authServerPort}/callback"
    private var idleLimit = 0
    private lateinit var timer: Timer

    private fun root(ctx: Context) {
        idleReset()
        ctx.result("Spotify4MC Web Authorizer is up and running")
    }

    private fun message(ctx: Context) {
        idleReset()
        ctx.result(ctx.queryParam("p").toString())
    }

    private fun login(ctx: Context) {
        idleReset()
        val state = UUID.randomUUID().toString().replace("-", "")
        val scope = URLEncoder.encode("user-read-private user-read-playback-state user-modify-playback-state", "UTF-8")
        val clientID = URLEncoder.encode(SpotifyConfig.instance.clientID, "UTF-8")

        ctx.redirect("https://accounts.spotify.com/authorize?" +
                "response_type=code&" +
                "client_id=${clientID}&" +
                "scope=${scope}&" +
                "redirect_uri=${redirect}&" +
                "state=$state")
    }

    private fun callback(ctx: Context) {
        idleReset()
        val code: String? = ctx.queryParam("code")
        val state: String = ctx.queryParam("state") ?: throw BadRequestResponse("State Mismatch")

        val authOptions = "code=$code&redirect_uri=$redirect&grant_type=authorization_code"

        try {
            val url = URI("https://accounts.spotify.com/api/token").toURL()
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            connection.setRequestProperty("Authorization", SpotifyApplication(SpotifyConfig.instance.clientID, SpotifyConfig.instance.clientSecret).authorization)

            connection.doOutput = true
            connection.outputStream.use { it.write(authOptions.toByteArray()) }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val responseBody = connection.inputStream.bufferedReader().use { it.readText() }
                val token = JsonManager.gson.fromJson(responseBody, SpotifyToken::class.java)
                SpotifyConfig.instance.token = token
                SpotifyConfig.instance.save()
                ctx.result("LOGIN COMPLETE")
                Thread { Thread.sleep(1000L); stop() }.start()
            } else {
                throw Exception("Unexpected response code: $responseCode ${connection.responseMessage}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ctx.status(HttpStatus.BAD_REQUEST)
        }
    }

    private fun idleReset() {
        idleLimit = 0
    }

    fun run() {
        timer = Timer()

        app = Javalin.create().start(SpotifyConfig.instance.authServerPort)
        app.get("/", this::root)
        app.get("/message", this::message)
        app.get("/login", this::login)
        app.get("/callback", this::callback)

        val task: TimerTask = object : TimerTask() {
            override fun run() {
                println(SpotifyConfig.instance.authServerIdleLimit - idleLimit)
                if (idleLimit >= SpotifyConfig.instance.authServerIdleLimit) {
                    stop()
                } else {
                    idleLimit++
                }
            }
        }
        timer.scheduleAtFixedRate(task, 0L, 1000L)
    }

    fun stop() {
        app.stop()
        timer.cancel()
    }
}
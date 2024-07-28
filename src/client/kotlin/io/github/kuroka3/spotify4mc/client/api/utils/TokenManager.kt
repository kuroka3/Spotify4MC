package io.github.kuroka3.spotify4mc.client.api.utils

import io.github.kuroka3.spotify4mc.client.utils.SpotifyConfig
import net.minecraft.util.Util
import java.net.URI

object TokenManager {
    fun login() {
        WebAuthorizer.run()
        Util.getOperatingSystem().open(URI("http://localhost:${SpotifyConfig.instance.authServerPort}/login"))
    }
}
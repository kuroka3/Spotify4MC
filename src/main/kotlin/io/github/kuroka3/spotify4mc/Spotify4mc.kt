package io.github.kuroka3.spotify4mc

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

class Spotify4mc : ModInitializer {

    private val logger = LoggerFactory.getLogger("assets/spotify4mc")

    override fun onInitialize() {
        logger.info("Initializing Spotify4MC")
    }
}

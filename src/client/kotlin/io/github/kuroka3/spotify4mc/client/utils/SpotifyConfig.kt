package io.github.kuroka3.spotify4mc.client.utils

import com.google.gson.GsonBuilder
import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import dev.isxander.yacl3.api.controller.StringControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import io.github.kuroka3.spotify4mc.client.api.classes.SpotifyToken
import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager
import io.github.kuroka3.spotify4mc.client.api.utils.TokenManager
import io.github.kuroka3.spotify4mc.client.indicator.ImageManager
import io.github.kuroka3.spotify4mc.client.indicator.IndicateManager
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class SpotifyConfig {
    companion object{
        val configFile = FabricLoader.getInstance().configDir.resolve("spotify_config.json5").toFile()
        val instance = SpotifyConfig().apply { load() }
    }

    var apiRequestInterval: Int = 20; private set
    var clientID: String = "YOUR_CLIENT_ID"; private set
    var clientSecret: String = "YOUR_CLIENT_SECRET"; private set
    var authServerPort: Int = 8080; private set
    var authServerIdleLimit = 300; private set
    var displayWidth = 250; private set
    var showAlbumArt = true; private set
    var showTrackInfo = true; private set
    var token: SpotifyToken = SpotifyToken("token", "Bearer", "scope", 3600, "refresh", System.currentTimeMillis())

    fun load() {
        if (!configFile.exists()) { return }
        val i = JsonManager.gson.fromJson<SpotifyConfig>(configFile.readText(), SpotifyConfig::class.java)
        this.apiRequestInterval = i.apiRequestInterval
        this.clientID = i.clientID
        this.clientSecret = i.clientSecret
        this.authServerPort = i.authServerPort
        this.authServerIdleLimit = i.authServerIdleLimit
        this.displayWidth = i.displayWidth
        this.showAlbumArt = i.showAlbumArt
        this.showTrackInfo = i.showTrackInfo
        this.token = i.token
    }

    fun save() {
        if (!configFile.exists()) {
            configFile.createNewFile()
        }
        val gson = GsonBuilder().setFieldNamingStrategy { it.name.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase() }.create()
        configFile.writeText(gson.toJson(this))
    }

    fun createGui(parent: Screen?): Screen {

        // Intervals
        val apiRequestIntervalOption = Option.createBuilder<Int>()
            .name(Text.literal("API Request Interval"))
            .description(OptionDescription.of(Text.literal("An interval between each API request when indicating now playing info")))
            .binding(
                20,
                { this.apiRequestInterval },
                { value -> this.apiRequestInterval = value }
            )
            .controller { option -> IntegerSliderControllerBuilder.create(option)
                .range(1, 100)
                .step(1)
                .formatValue { value -> Text.literal("$value ticks") }
            }.build()

        // Application
        val clientIDOption = Option.createBuilder<String>()
            .name(Text.literal("Client ID"))
            .description(OptionDescription.of(Text.literal("A Client ID to authorize token")))
            .binding(
                "YOUR_CLIENT_ID",
                { this.clientID },
                { value -> this.clientID = value }
            )
            .controller(StringControllerBuilder::create)
            .build()

        val clientSecretOption = Option.createBuilder<String>()
            .name(Text.literal("Client Secret"))
            .description(OptionDescription.of(Text.literal("A Client Secret to authorize token")))
            .binding(
                "YOUR_CLIENT_SECRET",
                { this.clientSecret },
                { value -> this.clientSecret = value }
            )
            .controller(StringControllerBuilder::create)
            .build()

        val authServerPortOption = Option.createBuilder<String>()
            .name(Text.literal("Authorizer Server Port"))
            .description(OptionDescription.of(Text.literal("A Port of Authorizer Web Server")))
            .binding(
                "8080",
                { this.authServerPort.toString() },
                { value -> this.authServerPort = if (value.toIntOrNull() != null) value.toInt() else this.authServerPort }
            )
            .controller(StringControllerBuilder::create)
            .build()

        val authServerIdleLimitOption = Option.createBuilder<Int>()
            .name(Text.literal("Authorizer Server Idle Limitation"))
            .description(OptionDescription.of(Text.literal("A Limit of Authorizer Web Server Idle State")))
            .binding(
                300,
                { this.authServerIdleLimit },
                { value -> this.authServerIdleLimit = value }
            )
            .controller { option -> IntegerSliderControllerBuilder.create(option)
                .range(30, 600)
                .step(10)
                .formatValue { value -> Text.literal("$value seconds") }
            }.build()

        val authorizeButton = ButtonOption.createBuilder()
            .name(Text.literal("Authorize"))
            .description(OptionDescription.of(Text.literal("Login with Spotify")))
            .text(Text.literal("Open Browser..."))
            .action { _, _ ->
                TokenManager.login()
            }.build()

        val debugTestButton = ButtonOption.createBuilder() //TODO Remove DebugBUtton
            .name(Text.literal("Debug Test"))
            .description(OptionDescription.of(Text.literal("Test Token")))
            .action { _, _ ->
                IndicateManager.startIndicate()
            }.build()

        // Display
        val displayWidthOption = Option.createBuilder<Int>()
            .name(Text.literal("Display Width"))
            .description(OptionDescription.of(Text.literal("A width of display container")))
            .binding(
                250,
                { this.displayWidth },
                { value -> this.displayWidth = value }
            )
            .controller { option -> IntegerSliderControllerBuilder.create(option)
                .range(50, MinecraftClient.getInstance().window.scaledWidth)
                .step(10)
                .formatValue { value -> Text.literal("$value px") }
            }.build()

        val showAlbumArtOption = Option.createBuilder<Boolean>()
            .name(Text.literal("Show Album Art"))
            .description(OptionDescription.of(Text.literal("Load and Show Album Art to HUD")))
            .binding(
                true,
                { this.showAlbumArt },
                { value -> this.showAlbumArt = value; ImageManager.reset() }
            )
            .controller(TickBoxControllerBuilder::create)
            .build()

        val showTrackInfoOption = Option.createBuilder<Boolean>()
            .name(Text.literal("Show Track Info"))
            .description(OptionDescription.of(Text.literal("Show Track Info like title, artist, etc. to HUD")))
            .binding(
                true,
                { this.showTrackInfo },
                { value -> this.showTrackInfo = value }
            )
            .controller(TickBoxControllerBuilder::create)
            .build()

        // Sensitive
        val tokenOption = Option.createBuilder<String>()
            .name(Text.literal("Token"))
            .description(OptionDescription.of(Text.literal("A Token to request API")))
            .binding(
                "YOUR_TOKEN",
                { this.token.accessToken },
                { _ -> }
            )
            .controller(StringControllerBuilder::create)
            .available(false)
            .build()

        return YetAnotherConfigLib.createBuilder()
            .title(Text.literal("Spotify4MC"))
            .category(
                ConfigCategory.createBuilder()
                    .name(Text.literal("Spotify"))
                    .tooltip(Text.literal("Default Settings for Spotify"))
                    .group(OptionGroup.createBuilder()
                        .name(Text.literal("Intervals"))
                        .description(OptionDescription.of(Text.literal("Group of Interval Settings")))
                        .option(apiRequestIntervalOption)
                        .build())
                    .group(OptionGroup.createBuilder()
                        .name(Text.literal("Application"))
                        .description(OptionDescription.of(Text.literal("Group of Application Info Settings")))
                        .option(clientIDOption)
                        .option(clientSecretOption)
                        .option(authServerPortOption)
                        .option(authServerIdleLimitOption)
                        .option(authorizeButton)
                        .option(debugTestButton)
                        .build())
                    .group(OptionGroup.createBuilder()
                        .name(Text.literal("Display"))
                        .description(OptionDescription.of(Text.literal("Group of Display Settings")))
                        .option(displayWidthOption)
                        .option(showAlbumArtOption)
                        .option(showTrackInfoOption)
                        .build())
                    .group(OptionGroup.createBuilder()
                        .name(Text.literal("Sensitive"))
                        .description(OptionDescription.of(Text.literal("Group of Sensitive Settings such as token")))
                        .option(tokenOption)
                        .collapsed(true)
                        .build())
                    .build())
            .save(this::save)
            .build()
            .generateScreen(parent)
    }
}
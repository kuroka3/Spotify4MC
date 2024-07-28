package io.github.kuroka3.spotify4mc.client.utils

import com.google.gson.GsonBuilder
import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import dev.isxander.yacl3.api.controller.StringControllerBuilder
import io.github.kuroka3.spotify4mc.client.api.utils.JsonManager
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class SpotifyConfig {
    companion object{
        val configFile = FabricLoader.getInstance().configDir.resolve("spotify_config.json5").toFile()
        val instance = SpotifyConfig().apply { load() }
    }

    var apiRequestInterval: Int = 20; private set
    var trackRefreshInterval: Int = 1; private set
    var clientID: String = "YOUR_CLIENT_ID"; private set
    var clientSecret: String = "YOUR_CLIENT_SECRET"; private set
    var token: String = "token"; private set

    fun load() {
        if (!configFile.exists()) { return }
        val i = JsonManager.gson.fromJson<SpotifyConfig>(configFile.readText(), SpotifyConfig::class.java)
        this.apiRequestInterval = i.apiRequestInterval
        this.trackRefreshInterval = i.trackRefreshInterval
        this.clientID = i.clientID
        this.clientSecret = i.clientSecret
        this.trackRefreshInterval = i.trackRefreshInterval
    }

    fun save() {
        if (!configFile.exists()) {
            configFile.createNewFile()
        }
        val gson = GsonBuilder().setFieldNamingStrategy { it.name.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase() }.create()
        configFile.writeText(gson.toJson(this))
    }

    fun createGui(parent: Screen?): Screen {
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

        val trackRefreshIntervalOption = Option.createBuilder<Int>()
            .name(Text.literal("Track Refresh Interval"))
            .description(OptionDescription.of(Text.literal("An interval between updating saved track progress info")))
            .binding(
                1,
                { this.trackRefreshInterval },
                { value -> this.trackRefreshInterval = value }
            )
            .controller { option -> IntegerSliderControllerBuilder.create(option)
                .range(1, 20)
                .step(1)
                .formatValue { value -> Text.literal("$value ticks") }
            }.build()

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

        val tokenOption = Option.createBuilder<String>()
            .name(Text.literal("Token"))
            .description(OptionDescription.of(Text.literal("A Token to request API")))
            .binding(
                "YOUR_TOKEN",
                { this.token },
                { value -> this.token = value }
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
                        .option(trackRefreshIntervalOption)
                        .build())
                    .group(OptionGroup.createBuilder()
                        .name(Text.literal("Application"))
                        .description(OptionDescription.of(Text.literal("Group of Application Info Settings")))
                        .option(clientIDOption)
                        .option(clientSecretOption)
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
package io.github.kuroka3.spotify4mc.client.api.utils

import com.google.gson.FieldNamingStrategy
import com.google.gson.GsonBuilder
import java.lang.reflect.Field

object JsonManager {
    val gson = GsonBuilder().setFieldNamingStrategy(SpotifyFieldNamingStrategy()).create()

    private class SpotifyFieldNamingStrategy : FieldNamingStrategy {
        override fun translateName(field: Field): String {
            return field.name.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
        }
    }
}
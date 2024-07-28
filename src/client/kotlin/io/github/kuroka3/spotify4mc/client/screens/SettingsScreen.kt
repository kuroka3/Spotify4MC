package io.github.kuroka3.spotify4mc.client.screens

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
class SettingsScreen : Screen(Text.literal("Settings Screen")) {
    lateinit var button1: ButtonWidget
    lateinit var button2: ButtonWidget

    lateinit var textfield: TextFieldWidget

    override fun init() {
        println("${width}x${height}")

        button1 = ButtonWidget.builder(Text.literal("sex!")
        ) { println("He clicked 1") }
            .dimensions((width / 2) - (width / 4), height / 2, 200, 20)
            .tooltip(Tooltip.of(Text.literal("Tooltip of button1")))
            .build()
        button2 = ButtonWidget.builder(Text.literal("on the bitch!")
        ) { println(textfield.text) }
            .dimensions((width / 2), 0, 200, 20)
            .tooltip(Tooltip.of(Text.literal("Tooltip of button2")))
            .build()

        textfield = TextFieldWidget(MinecraftClient.getInstance().textRenderer, (width / 2), 40, 200, 20, Text.literal("yes!"))

        addDrawableChild(button1)
        addDrawableChild(button2)
        addDrawableChild(textfield)
    }
}
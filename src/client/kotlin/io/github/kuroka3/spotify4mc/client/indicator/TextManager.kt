package io.github.kuroka3.spotify4mc.client.indicator

import net.minecraft.client.font.TextRenderer
import net.minecraft.text.OrderedText
import net.minecraft.text.Text

object TextManager {
    fun calculateTitle(renderer: TextRenderer, name: Text, artist: Text, sizeLimit: Int): Pair<Int, Pair<OrderedText, OrderedText>> {
        val nameWrapped = renderer.wrapLines(name, sizeLimit)
        val artistWrapped = renderer.wrapLines(artist, sizeLimit)

        val nameSizeWrapped = renderer.getWidth(nameWrapped[0])
        val artistSizeWrapped = renderer.getWidth(artistWrapped[0])
        val titleSize = if(nameSizeWrapped > artistSizeWrapped) nameSizeWrapped else artistSizeWrapped

        return Pair(titleSize, Pair(nameWrapped[0], artistWrapped[0]))
    }
}
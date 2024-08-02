package io.github.kuroka3.spotify4mc.client.indicator

import java.awt.image.BufferedImage
import java.util.*


object ColorManager {
    fun findDominantColor(image: BufferedImage): Int {
        val height = image.height
        val width = image.width

        val m: MutableMap<Int, Int> = HashMap()
        for (i in 0 until width) {
            for (j in 0 until height) {
                val rgb = image.getRGB(i, j)
                val rgbArr: IntArray = getRGBArr(rgb)

                // Filter out grays....
                if (!isGray(rgbArr)) {
                    var counter = m[rgb]
                    if (counter == null) counter = 0
                    counter++
                    m[rgb] = counter
                }
            }
        }
        return getMostCommonColour(m)
    }

    fun fixColor(color: Int): Int {
        val alpha = (color shr 24) and 0xFF
        val red = (color shr 16) and 0xFF
        val green = (color shr 8) and 0xFF
        val blue = color and 0xFF

        // 색상 채널 순서를 조정
        return (alpha shl 24) or (blue shl 16) or (green shl 8) or red
    }

    fun addAlphaToHexColor(hexColor: Int, alpha: Int): Int {
        // 알파값을 0-255 범위로 제한
        val clampedAlpha = alpha.coerceIn(0, 255)

        // 기존 색상에서 RGB를 추출
        val red = (hexColor shr 16) and 0xFF
        val green = (hexColor shr 8) and 0xFF
        val blue = hexColor and 0xFF

        // 새로운 ARGB 형식으로 색상 생성
        return (clampedAlpha shl 24) or (red shl 16) or (green shl 8) or blue
    }

    private fun getMostCommonColour(map: Map<Int, Int>): Int {
        try {
            val list: MutableList<*> = LinkedList<Any?>(map.entries)
            list.sortWith(Comparator { o1, o2 -> Int
                return@Comparator (o1 as Map.Entry<Int, Int>).value.compareTo((o2 as Map.Entry<Int, Int>).value)
            })
            val me = list[list.size - 1] as Map.Entry<Int, Int>
            return me.key
        } catch (e: IndexOutOfBoundsException) {
            return 0x00191414
        }
    }

    private fun getRGBArr(rgb: Int): IntArray {
        val alpha: Int = (rgb shr 24) and 0xff
        val red: Int = (rgb shr 16) and 0xff
        val green: Int = (rgb shr 8) and 0xff
        val blue: Int = (rgb) and 0xff
        return intArrayOf(red, green, blue)
    }

    private fun isGray(rgbArr: IntArray): Boolean {
        val rgDiff = rgbArr[0] - rgbArr[1]
        val rbDiff = rgbArr[0] - rgbArr[2]
        // Filter out black, white and grays...... (tolerance within 10 pixels)
        val tolerance = 10
        if (rgDiff > tolerance || rgDiff < -tolerance) if (rbDiff > tolerance || rbDiff < -tolerance) {
            return false
        }
        return true
    }
}
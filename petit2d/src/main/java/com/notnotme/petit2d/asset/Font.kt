package com.notnotme.petit2d.asset

import com.notnotme.petit2d.asset.base.Asset
import com.notnotme.petit2d.model.TextureRegion


class Font internal constructor(
    internal val characters     : HashMap<Char, TextureRegion>,
    internal var texture        : Texture,
    internal var fontSpacing    : Float
) : Asset {
    fun getFontSpacing(): Float {
        return fontSpacing
    }

    fun bind(unit: Int) {
        texture.bind(unit)
    }

    fun measure(text: String): Int {
        return text.sumOf { get(it).width }
    }

    fun get(char: Char): TextureRegion {
        return characters[char] ?: throw IndexOutOfBoundsException("$char not found in characters")
    }
}

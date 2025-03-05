package com.notnotme.petit2d

import android.content.Context
import com.notnotme.petit2d.asset.loader.CatalogLoader
import com.notnotme.petit2d.asset.loader.FontLoader
import com.notnotme.petit2d.asset.loader.TextureLoader


class AssetManager(
    private val context: Context
) {
    val quadProgram     : QuadProgram   = QuadProgram()
    val spriteProgram   : SpriteProgram = SpriteProgram()
    val fontLoader      : FontLoader    = FontLoader()
    val textureLoader   : TextureLoader = TextureLoader()
    val catalogLoader   : CatalogLoader = CatalogLoader()
    val manager         get()           = context.assets

    internal fun create() {
        quadProgram.create()
        spriteProgram.create()
    }

    internal fun destroy() {
        quadProgram.destroy()
        spriteProgram.destroy()
    }
}

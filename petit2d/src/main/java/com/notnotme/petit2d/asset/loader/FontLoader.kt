package com.notnotme.petit2d.asset.loader

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Typeface
import com.notnotme.petit2d.asset.Font
import com.notnotme.petit2d.asset.Texture
import com.notnotme.petit2d.asset.base.AssetLoader
import com.notnotme.petit2d.glkt.createTexture
import com.notnotme.petit2d.glkt.deleteTexture
import com.notnotme.petit2d.model.TextureRegion
import kotlin.math.abs


class FontLoader : AssetLoader<Font> {
    private val fonts       : HashMap<String, Font>     = hashMapOf()
    private val paint       : Paint                     = Paint().also { it.color = Color.WHITE }
    private var fontSize    : Float                     = 16.0f

    fun setAntiAliasingMode(enabled: Boolean) {
        paint.isAntiAlias = enabled
        paint.isDither = enabled
        paint.isSubpixelText = enabled
    }

    fun setFontSize(size: Int) {
        fontSize = size.toFloat()
    }

    override fun load(assetManager: AssetManager, path: String): Font {
        return  fonts[path] ?: run {
            Typeface.createFromAsset(assetManager, path).let { face ->
                paint.typeface = face
                paint.textSize = fontSize

                val bitmap = Bitmap.createBitmap(DEFAULT_TEXTURE_SIZE, DEFAULT_TEXTURE_SIZE, Bitmap.Config.ALPHA_8)
                val canvas = Canvas(bitmap)
                val metrics = paint.fontMetrics
                val fontHeight = paint.fontSpacing.toInt()
                val pen = PointF(TEXTURE_CHAR_SPACING, TEXTURE_CHAR_SPACING)
                val characters = hashMapOf<Char, TextureRegion>()

                canvas.drawColor(Color.TRANSPARENT)
                for (i in 0 until 255) {
                    val char = Char(i)
                    val stringChar = char.toString()
                    val charWidth = paint.measureText(stringChar).toInt()

                    val potWidth = charWidth + 1 and 1.inv()
                    val potHeight = fontHeight + 1 and 1.inv()
                    if (pen.x + potWidth + TEXTURE_CHAR_SPACING > bitmap.width) {
                        if (pen.y + potHeight + TEXTURE_CHAR_SPACING > bitmap.height) {
                            throw IllegalStateException("Bitmap is too small")
                        }

                        pen.x = TEXTURE_CHAR_SPACING
                        pen.y += potHeight + TEXTURE_CHAR_SPACING
                    }

                    canvas.drawText(stringChar, pen.x, pen.y + (fontHeight - abs(metrics.descent)), paint)
                    characters[char] = TextureRegion.obtain().apply {
                        width = potWidth
                        height = potHeight
                        s = pen.x / bitmap.width.toFloat()
                        t = pen.y / bitmap.height.toFloat()
                        p = (pen.x + width.toFloat()) / bitmap.width.toFloat()
                        q = (pen.y + height.toFloat()) / bitmap.height.toFloat()
                    }

                    pen.x += charWidth + TEXTURE_CHAR_SPACING
                }

                val texture = createTexture(bitmap)
                bitmap.recycle()

                Font(characters, Texture(texture), paint.fontSpacing).also {
                    fonts[path] = it
                }
            }
        }
    }

    override fun get(path: String): Font {
        return fonts[path] ?: throw IndexOutOfBoundsException("$path not found in fonts")
    }

    override fun unload(path: String) {
        fonts.remove(path)?.apply {
            characters.values.forEach(TextureRegion::recycle)
            characters.clear()

            deleteTexture(texture.texture)
            texture.texture = 0

            fontSpacing = 0.0f
        }
    }

    companion object {
        const val TEXTURE_CHAR_SPACING  = 1.0f
        const val DEFAULT_TEXTURE_SIZE  = 512
    }
}

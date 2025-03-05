package com.notnotme.petit2d.otom4t

import com.notnotme.otom4t.Node
import com.notnotme.petit2d.asset.Font
import com.notnotme.petit2d.model.Sprite


class CreateText (
    private val fontProvider    : () -> Font,
    private val textureUnit     : Int,
    private val sprites         : MutableList<Sprite>,
    private val x               : Float,
    private val y               : Float,
    private val text            : String
) : Node {
    override fun execute(dt: Float): Node.Status {
        val font        = fontProvider()
        val textWidth   = font.measure(text)
        val spacing     = font.getFontSpacing()
        var positionX   = x - (textWidth / 2.0f)
        val positionY   = y - (spacing / 2.0f)

        text.map { c ->
            val region = font.get(c)
            Sprite.obtain().apply {
                position.set(
                    positionX + (region.width / 2.0f),
                    positionY + spacing / 2.0f
                )

                size.set(
                    region.width,
                    region.height
                )

                texture.set(
                    region.s,
                    region.t,
                    region.p,
                    region.q
                )

                unit = textureUnit

                positionX += region.width
            }
        }.run(sprites::addAll)

        return  Node.Status.Success
    }
}

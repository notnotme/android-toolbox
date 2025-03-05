package com.notnotme.petit2d.otom4t

import com.notnotme.otom4t.Node
import com.notnotme.petit2d.model.Sprite


class SetSpriteTexture(
    private val sprite  : Sprite,
    private val s       : Float,
    private val t       : Float,
    private val p       : Float,
    private val q       : Float
) : Node {
    override fun execute(dt: Float): Node.Status {
        sprite.texture.set(s, t, p, q)
        return Node.Status.Success
    }
}

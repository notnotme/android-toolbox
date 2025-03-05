package com.notnotme.petit2d.otom4t

import com.notnotme.otom4t.Node
import com.notnotme.petit2d.model.Sprite


class SetSpritePosition(
    private val sprite  : Sprite,
    private val x       : Float,
    private val y       : Float
) : Node {
    override fun execute(dt: Float): Node.Status {
        sprite.position.set(x, y)
        return Node.Status.Success
    }
}

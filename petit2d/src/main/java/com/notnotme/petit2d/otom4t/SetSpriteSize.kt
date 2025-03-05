package com.notnotme.petit2d.otom4t

import com.notnotme.otom4t.Node
import com.notnotme.petit2d.model.Sprite


class SetSpriteSize(
    private val sprite  : Sprite,
    private val width   : Int,
    private val height  : Int
) : Node {
    override fun execute(dt: Float): Node.Status {
        sprite.size.set(width, height)
        return Node.Status.Success
    }
}

package com.notnotme.petit2d.otom4t

import com.notnotme.otom4t.Node
import com.notnotme.petit2d.model.Sprite


class SetSpriteUnit(
    private val sprite: Sprite,
    private val unit: Int
) : Node {
    override fun execute(dt: Float): Node.Status {
        sprite.unit = unit
        return  Node.Status.Success
    }
}
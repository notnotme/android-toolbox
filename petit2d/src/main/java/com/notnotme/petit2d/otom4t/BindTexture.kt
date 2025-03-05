package com.notnotme.petit2d.otom4t

import com.notnotme.otom4t.Node
import com.notnotme.petit2d.asset.Texture


class BindTexture (
    private val textureProvider : () -> Texture,
    private val unit            : Int
) : Node {
    override fun execute(dt: Float): Node.Status {
        textureProvider().bind(unit)
        return  Node.Status.Success
    }
}

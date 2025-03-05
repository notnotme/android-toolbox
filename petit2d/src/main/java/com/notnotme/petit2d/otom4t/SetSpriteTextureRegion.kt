package com.notnotme.petit2d.otom4t

import com.notnotme.otom4t.Node
import com.notnotme.petit2d.model.Sprite
import com.notnotme.petit2d.model.TextureRegion


class SetSpriteTextureRegion(
    private val sprite  : Sprite,
    private val region  : TextureRegion
) : Node {
    override fun execute(dt: Float): Node.Status {
        sprite.size.set(region.width, region.height)
        sprite.texture.set(region.s, region.t, region.p, region.q)
        return Node.Status.Success
    }
}

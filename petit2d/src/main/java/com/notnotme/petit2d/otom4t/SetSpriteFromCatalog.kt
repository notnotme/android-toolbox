package com.notnotme.petit2d.otom4t

import com.notnotme.otom4t.Node
import com.notnotme.petit2d.asset.Catalog
import com.notnotme.petit2d.model.Sprite


class SetSpriteFromCatalog(
    private val catalogProvider : () -> Catalog,
    private val sprite          : Sprite,
    private val name            : String,
) : Node {
    override fun execute(dt: Float): Node.Status {
        val region = catalogProvider().get(name)
        sprite.size.set(region.width, region.height)
        sprite.texture.set(region.s, region.t, region.p, region.q)
        return Node.Status.Success
    }
}

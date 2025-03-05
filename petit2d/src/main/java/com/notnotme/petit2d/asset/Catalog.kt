package com.notnotme.petit2d.asset

import com.notnotme.petit2d.asset.base.Asset
import com.notnotme.petit2d.model.TextureRegion


class Catalog internal constructor(
    internal val regions: HashMap<String, TextureRegion>
) : Asset {
    fun get(name: String): TextureRegion {
        return regions[name] ?: throw IndexOutOfBoundsException("$name not found in catalog")
    }
}

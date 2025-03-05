package com.notnotme.petit2d.scene.base

import com.notnotme.petit2d.AssetManager
import com.notnotme.petit2d.asset.Texture


abstract class BaseTransition(
    width                       : Int,
    height                      : Int,
    protected val outTexture    : Texture,
    protected val inTexture     : Texture
) : Scene(width, height) {
    abstract fun enter(assets: AssetManager)
    abstract fun exit(assets: AssetManager)
    abstract fun update(dt: Float)
    abstract fun isFinished(): Boolean
}

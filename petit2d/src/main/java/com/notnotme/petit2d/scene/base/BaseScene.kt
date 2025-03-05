package com.notnotme.petit2d.scene.base

import com.notnotme.petit2d.AssetManager
import com.notnotme.petit2d.scene.SceneChanger


abstract class BaseScene(
    width   : Int,
    height  : Int,
) : Scene(width, height) {
    abstract fun enter(assets: AssetManager, changer: SceneChanger)
    abstract fun exit(assets: AssetManager)
    abstract fun update(dt: Float)
}

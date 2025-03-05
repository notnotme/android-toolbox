package com.notnotme.petit2d.otom4t

import com.notnotme.otom4t.Node
import com.notnotme.petit2d.scene.SceneChanger


class ChangeScene (
    private val sceneChangerProvider    : () -> SceneChanger,
    private val transitionId            : Int,
    private val sceneId                 : Int
) : Node {
    override fun execute(dt: Float): Node.Status {
        sceneChangerProvider().change(transitionId, sceneId)
        return  Node.Status.Success
    }
}

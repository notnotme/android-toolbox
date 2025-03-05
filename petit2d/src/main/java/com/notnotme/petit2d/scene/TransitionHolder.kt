package com.notnotme.petit2d.scene

import com.notnotme.petit2d.scene.base.BaseScene
import com.notnotme.petit2d.scene.base.BaseTransition


internal data class TransitionHolder(
    val inScene     : BaseScene,
    val transition  : BaseTransition
)

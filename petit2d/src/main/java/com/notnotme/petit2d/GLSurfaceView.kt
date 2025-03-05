package com.notnotme.petit2d

import android.content.Context
import android.util.AttributeSet


class GLSurfaceView : android.opengl.GLSurfaceView {
    constructor(context: Context) : super(context) {
        configureEgl()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        configureEgl()
    }

    constructor(context: Context, renderer: Renderer) : super(context) {
        configureEgl()
        setRenderer(renderer)
    }

    override fun setRenderer(renderer: Renderer) {
        super.setRenderer(renderer)
        id = R.id.surface_view
        preserveEGLContextOnPause = true
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    private fun configureEgl() {
        setEGLContextClientVersion(3)
        setEGLConfigChooser(8, 8, 8, 0, 0, 0)
    }
}

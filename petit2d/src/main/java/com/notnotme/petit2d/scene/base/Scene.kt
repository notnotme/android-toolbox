package com.notnotme.petit2d.scene.base

import android.opengl.GLES32.GL_FRAMEBUFFER
import android.opengl.GLES32.glBindFramebuffer
import com.notnotme.petit2d.glkt.createFramebuffer
import com.notnotme.petit2d.glkt.createTexture
import com.notnotme.petit2d.glkt.deleteFramebuffer
import com.notnotme.petit2d.glkt.deleteTexture


abstract class Scene(
    val width   : Int,
    val height  : Int,
) {
    private var framebufferTexture  : Int   = 0
    private var framebuffer         : Int   = 0
    internal val texture            get()   = framebufferTexture

    internal fun create() {
        framebufferTexture = createTexture(width, height, 4)
        framebuffer = createFramebuffer(framebufferTexture)
    }

    internal fun destroy() {
        deleteFramebuffer(framebuffer)
        framebuffer = 0

        deleteTexture(framebufferTexture)
        framebufferTexture = 0
    }

    protected fun bindFrameBuffer() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer)
    }
}

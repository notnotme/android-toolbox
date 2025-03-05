package com.notnotme.petit2d.glkt

import android.opengl.GLES32.GL_COLOR_ATTACHMENT0
import android.opengl.GLES32.GL_FRAMEBUFFER
import android.opengl.GLES32.GL_FRAMEBUFFER_COMPLETE
import android.opengl.GLES32.GL_TEXTURE_2D
import android.opengl.GLES32.glBindFramebuffer
import android.opengl.GLES32.glCheckFramebufferStatus
import android.opengl.GLES32.glDeleteFramebuffers
import android.opengl.GLES32.glFramebufferTexture2D
import android.opengl.GLES32.glGenFramebuffers


fun createFramebuffer(texture: Int, attachment: Int = GL_COLOR_ATTACHMENT0): Int {
    val framebufferId = intArrayOf(0)
    glGenFramebuffers(1, framebufferId, 0)
    glBindFramebuffer(GL_FRAMEBUFFER, framebufferId[0])
    glFramebufferTexture2D(
        GL_FRAMEBUFFER,
        attachment,
        GL_TEXTURE_2D,
        texture,
        0
    )

    val status = glCheckFramebufferStatus(GL_FRAMEBUFFER)
    if (status != GL_FRAMEBUFFER_COMPLETE) {
        throw IllegalStateException("Framebuffer is not complete: $status")
    }

    return framebufferId[0]
}

fun deleteFramebuffer(framebuffer: Int) {
    val framebufferId = intArrayOf(framebuffer)
    glDeleteFramebuffers(1, framebufferId, 0)
}

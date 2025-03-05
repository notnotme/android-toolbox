package com.notnotme.petit2d.glkt

import android.graphics.Bitmap
import android.opengl.GLES32.GL_CLAMP_TO_EDGE
import android.opengl.GLES32.GL_NEAREST
import android.opengl.GLES32.GL_R8
import android.opengl.GLES32.GL_RED
import android.opengl.GLES32.GL_RGB
import android.opengl.GLES32.GL_RGB8
import android.opengl.GLES32.GL_RGBA
import android.opengl.GLES32.GL_RGBA8
import android.opengl.GLES32.GL_TEXTURE0
import android.opengl.GLES32.GL_TEXTURE_2D
import android.opengl.GLES32.GL_TEXTURE_MAG_FILTER
import android.opengl.GLES32.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES32.GL_TEXTURE_WRAP_S
import android.opengl.GLES32.GL_TEXTURE_WRAP_T
import android.opengl.GLES32.GL_UNSIGNED_BYTE
import android.opengl.GLES32.glActiveTexture
import android.opengl.GLES32.glBindTexture
import android.opengl.GLES32.glDeleteTextures
import android.opengl.GLES32.glGenTextures
import android.opengl.GLES32.glTexImage2D
import android.opengl.GLES32.glTexParameteri
import android.opengl.GLUtils


fun createTexture(width: Int, height: Int, bpp: Int): Int {
    val format = when (bpp) {
        1 -> GL_RED
        3 -> GL_RGB
        4 -> GL_RGBA
        else -> throw IllegalArgumentException("Unsupported bpp: $bpp")
    }

    val internalFormat = when (format) {
        GL_RED -> GL_R8
        GL_RGB -> GL_RGB8
        GL_RGBA -> GL_RGBA8
        else -> throw IllegalArgumentException("Unsupported format: $format")
    }

    val textureId = intArrayOf(0)
    glGenTextures(1, textureId, 0)
    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, textureId[0])

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
    glTexImage2D(
        GL_TEXTURE_2D,
        0,
        internalFormat,
        width,
        height,
        0,
        format,
        GL_UNSIGNED_BYTE,
        null)

    return textureId[0]
}

fun createTexture(bitmap: Bitmap): Int {
    val textureId = intArrayOf(0)
    glGenTextures(1, textureId, 0)
    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, textureId[0])

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
    GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)

    return textureId[0]
}

fun deleteTexture(texture: Int) {
    val textureId = intArrayOf(texture)
    glDeleteTextures(1, textureId, 0)
}

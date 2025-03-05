package com.notnotme.petit2d.glkt

import android.opengl.GLES20
import android.opengl.GLES32.glGenBuffers


fun createBuffer(): Int {
    val bufferId = intArrayOf(0)
    glGenBuffers(1, bufferId, 0)
    return  bufferId[0]
}

fun deleteBuffer(buffer: Int) {
    val bufferId = intArrayOf(buffer)
    GLES20.glDeleteBuffers(1, bufferId, 0)
}

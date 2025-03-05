package com.notnotme.petit2d.glkt

import android.opengl.GLES32.GL_LINK_STATUS
import android.opengl.GLES32.GL_TRUE
import android.opengl.GLES32.glDeleteProgram
import android.opengl.GLES32.glGetProgramInfoLog
import android.opengl.GLES32.glGetProgramiv


fun checkProgram(program: Int) {
    val linkStatus = intArrayOf(0)

    glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0)
    if (linkStatus[0] != GL_TRUE) {
        val error = glGetProgramInfoLog(program)
        glDeleteProgram(program)
        throw IllegalStateException("Could not link program: $error")
    }
}

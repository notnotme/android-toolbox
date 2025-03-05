package com.notnotme.petit2d.glkt

import android.opengl.GLES32.GL_COMPILE_STATUS
import android.opengl.GLES32.glCompileShader
import android.opengl.GLES32.glCreateShader
import android.opengl.GLES32.glDeleteShader
import android.opengl.GLES32.glGetShaderInfoLog
import android.opengl.GLES32.glGetShaderiv
import android.opengl.GLES32.glShaderSource


fun compileShader(type: Int, source: String): Int {
    val shader = glCreateShader(type)
    glShaderSource(shader, source)
    glCompileShader(shader)

    // Check compile status
    val compiled = intArrayOf(0)
    glGetShaderiv(shader, GL_COMPILE_STATUS, compiled, 0)
    if (compiled[0] == 0) {
        val error = glGetShaderInfoLog(shader)
        glDeleteShader(shader)
        throw IllegalStateException("Could not compile shader : $error")
    }

    return shader
}

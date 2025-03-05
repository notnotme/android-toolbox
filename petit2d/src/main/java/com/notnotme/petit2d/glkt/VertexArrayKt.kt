package com.notnotme.petit2d.glkt

import android.opengl.GLES32.glDeleteVertexArrays
import android.opengl.GLES32.glGenVertexArrays


fun createVertexArray(): Int {
    val vertexArrayId = intArrayOf(0)
    glGenVertexArrays(1, vertexArrayId, 0)
    return vertexArrayId[0]
}

fun deleteVertexArray(vertexArray: Int) {
    val vertexArrayId = intArrayOf(vertexArray)
    glDeleteVertexArrays(1, vertexArrayId, 0)
}

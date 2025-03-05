package com.notnotme.petit2d.glkt

import android.opengl.Matrix


fun FloatArray.orthoM(width: Float, height: Float) = Matrix.orthoM(
    this,
    0,
    0.0f,
    width,
    height,
    0.0f,
    0.0f,
    1.0f
)

fun orthoM(width: Float, height: Float) = FloatArray(16).apply {
    Matrix.orthoM(
        this,
        0,
        0.0f,
        width,
        height,
        0.0f,
        0.0f,
        1.0f
    )
}

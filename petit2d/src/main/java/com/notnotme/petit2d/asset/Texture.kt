package com.notnotme.petit2d.asset

import android.opengl.GLES32.GL_TEXTURE0
import android.opengl.GLES32.GL_TEXTURE_2D
import android.opengl.GLES32.glActiveTexture
import android.opengl.GLES32.glBindTexture
import com.notnotme.petit2d.asset.base.Asset


class Texture internal constructor(
    internal var texture : Int
) : Asset {
    fun bind(unit: Int) {
        glActiveTexture(GL_TEXTURE0 + unit)
        glBindTexture(GL_TEXTURE_2D, texture)
    }
}

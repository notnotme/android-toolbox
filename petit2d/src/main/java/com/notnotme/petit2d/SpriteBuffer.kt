package com.notnotme.petit2d

import android.annotation.SuppressLint
import android.opengl.GLES32.GL_ARRAY_BUFFER
import android.opengl.GLES32.GL_DYNAMIC_DRAW
import android.opengl.GLES32.GL_MAP_FLUSH_EXPLICIT_BIT
import android.opengl.GLES32.GL_MAP_INVALIDATE_BUFFER_BIT
import android.opengl.GLES32.GL_MAP_INVALIDATE_RANGE_BIT
import android.opengl.GLES32.GL_MAP_WRITE_BIT
import android.opengl.GLES32.glBindBuffer
import android.opengl.GLES32.glBufferData
import android.opengl.GLES32.glFlushMappedBufferRange
import android.opengl.GLES32.glMapBufferRange
import android.opengl.GLES32.glUnmapBuffer
import com.notnotme.petit2d.glkt.createBuffer
import com.notnotme.petit2d.glkt.deleteBuffer
import com.notnotme.petit2d.model.Sprite
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.ceil
import kotlin.math.floor


class SpriteBuffer(
    private val capacity: Int
) {
    private var bufferData      : ByteBuffer?   = null
    private var bufferId        : Int           = 0
    val id                      get()           = bufferId
    val index                   get()           = bufferData?.position()?.div(SpriteProgram.STRIDE_SIZE) ?: 0
    val size                    get()           = bufferData?.capacity()?.div(SpriteProgram.STRIDE_SIZE) ?: 0

    fun create() {
        bufferId = createBuffer()
        glBindBuffer(GL_ARRAY_BUFFER, bufferId)
        glBufferData(
            GL_ARRAY_BUFFER,
            capacity * SpriteProgram.STRIDE_SIZE,
            null,
            GL_DYNAMIC_DRAW
        )
    }

    fun destroy() {
        deleteBuffer(bufferId)
        bufferId = 0
    }

    fun use() {
        glBindBuffer(GL_ARRAY_BUFFER, bufferId)
        glMapBufferRange(
            GL_ARRAY_BUFFER,
            0,
            capacity * SpriteProgram.STRIDE_SIZE,
            GL_MAP_WRITE_BIT or GL_MAP_INVALIDATE_RANGE_BIT or GL_MAP_FLUSH_EXPLICIT_BIT
        )?.let {
            bufferData = (it as ByteBuffer).order(ByteOrder.nativeOrder())
        }
    }

    fun start() {
        bufferData?.position(0)
    }

    fun end() {
        glFlushMappedBufferRange(GL_ARRAY_BUFFER, 0, index * SpriteProgram.STRIDE_SIZE)
        glUnmapBuffer(GL_ARRAY_BUFFER)
    }

    @SuppressLint("HalfFloat")
    fun putSprite(sprite: Sprite) {
        bufferData?.apply {
            putFloat(sprite.position.x)
            putFloat(sprite.position.y)
            putFloat(sprite.rotation)
            putShort(ceil(sprite.texture.left * Short.MAX_VALUE).toInt().toShort())
            putShort(ceil(sprite.texture.top * Short.MAX_VALUE).toInt().toShort())
            putShort(floor(sprite.texture.right * Short.MAX_VALUE).toInt().toShort())
            putShort(floor(sprite.texture.bottom * Short.MAX_VALUE).toInt().toShort())
            putShort((sprite.size.x * sprite.scale.x).toInt().toShort())
            putShort((sprite.size.y * sprite.scale.y).toInt().toShort())
            put((sprite.tint.red() * 255).toInt().toByte())
            put((sprite.tint.green() * 255).toInt().toByte())
            put((sprite.tint.blue() * 255).toInt().toByte())
            put((sprite.tint.alpha() * 255).toInt().toByte())
            put(sprite.unit.toByte())
            put(0) // pad to 32 bytes
            put(0) // pad to 32 bytes
            put(0) // pad to 32 bytes
        }
    }
}

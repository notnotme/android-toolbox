package com.notnotme.petit2d.model

import android.graphics.Color
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import androidx.core.util.Pools


data class Sprite(
    var position    : PointF  = PointF(0.0f, 0.0f),
    var scale       : PointF  = PointF(1.0f, 1.0f),
    var texture     : RectF   = RectF(0.0f, 0.0f, 1.0f, 1.0f),
    var size        : Point   = Point(0, 0),
    var rotation    : Float   = 0.0f,
    var unit        : Int     = 0,
    var tint        : Color   = Color.valueOf(Color.WHITE)
) {
    fun recycle() {
        pool.release(this)
    }

    companion object {
        private val pool = Pools.SimplePool<Sprite>(256)

        fun obtain(): Sprite {
            return pool.acquire() ?: Sprite()
        }
    }
}

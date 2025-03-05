package com.notnotme.petit2d.model

import androidx.core.util.Pools


data class TextureRegion(
    var width   : Int   = 0,
    var height  : Int   = 0,
    var s       : Float = 0.0f,
    var t       : Float = 0.0f,
    var p       : Float = 0.0f,
    var q       : Float = 0.0f
) {
    fun recycle() {
        pool.release(this)
    }

    companion object {
        private val pool = Pools.SimplePool<TextureRegion>(256)

        fun obtain(): TextureRegion {
            return pool.acquire() ?: TextureRegion()
        }
    }
}

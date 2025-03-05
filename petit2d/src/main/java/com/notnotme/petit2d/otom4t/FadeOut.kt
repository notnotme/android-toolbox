package com.notnotme.petit2d.otom4t

import android.graphics.Color
import android.view.animation.LinearInterpolator
import com.notnotme.otom4t.Node
import com.notnotme.petit2d.model.Sprite


class FadeOut(
    private val sprite: Sprite,
    duration: Float
) : Node {
    private var time            : Float                 = 0.0f
    private val step            : Float                 = 1.0f / duration
    private val interpolator    : LinearInterpolator    = LinearInterpolator()

    override fun execute(dt: Float): Node.Status {
        time += dt * step
        val value = (1.0f - interpolator.getInterpolation(time)).coerceIn(0.0f, 1.0f)

        val red = sprite.tint.red().let {
            if (it > value) {
                value
            } else {
                it
            }
        }
        val green = sprite.tint.green().let {
            if (it > value) {
                value
            } else {
                it
            }
        }
        val blue = sprite.tint.blue().let {
            if (it > value) {
                value
            } else {
                it
            }
        }

        sprite.tint = Color.valueOf(red, green, blue, 1.0f)

        return if (red == 0.0f && green == 0.0f && blue == 0.0f) {
            Node.Status.Success
        } else {
            Node.Status.Running
        }
    }
}

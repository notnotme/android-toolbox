package com.notnotme.petit2d.scene.transition

import android.opengl.GLES32.glScissor
import android.opengl.GLES32.glViewport
import com.notnotme.otom4t.Delay
import com.notnotme.otom4t.Node
import com.notnotme.otom4t.ParallelNode
import com.notnotme.otom4t.SequenceNode
import com.notnotme.petit2d.AssetManager
import com.notnotme.petit2d.SpriteBuffer
import com.notnotme.petit2d.SpriteProgram
import com.notnotme.petit2d.asset.Texture
import com.notnotme.petit2d.glkt.orthoM
import com.notnotme.petit2d.model.Sprite
import com.notnotme.petit2d.otom4t.BindTexture
import com.notnotme.petit2d.otom4t.FadeIn
import com.notnotme.petit2d.otom4t.FadeOut
import com.notnotme.petit2d.otom4t.SetSpritePosition
import com.notnotme.petit2d.otom4t.SetSpriteSize
import com.notnotme.petit2d.otom4t.SetSpriteTexture
import com.notnotme.petit2d.otom4t.SetSpriteUnit
import com.notnotme.petit2d.scene.base.BaseTransition


class FadeOutFadeIn(
    width                   : Int,
    height                  : Int,
    outTexture              : Texture,
    inTexture               : Texture,
    duration                : Float,
    stopDelay               : Float,
) : BaseTransition(width, height, outTexture, inTexture) {
    private val spriteBuffer    : SpriteBuffer      = SpriteBuffer(1)
    private val matrix          : FloatArray        = orthoM(width.toFloat(), height.toFloat())
    private val sprite          : Sprite            = Sprite.obtain()
    private val tree            : SequenceNode      = SequenceNode(
        ParallelNode(
            SetSpriteSize(sprite, width, height),
            SetSpriteTexture(sprite, 0.0f, 1.0f, 1.0f, 0.0f),
            SetSpritePosition(sprite, width * 0.5f, height * 0.5f),
            SetSpriteUnit(sprite, 0),
            BindTexture({outTexture}, 0)
        ),
        ParallelNode(
            BindTexture({outTexture}, 0),
            FadeOut(sprite, duration),
        ),
        ParallelNode(
            BindTexture({inTexture}, 0),
            Delay(stopDelay),
        ),
        ParallelNode(
            BindTexture({inTexture}, 0),
            FadeIn(sprite, duration)
        ),
    )

    private lateinit var spriteProgram  : SpriteProgram

    override fun enter(assets: AssetManager) {
        spriteProgram = assets.spriteProgram
        spriteBuffer.create()
    }

    override fun exit(assets: AssetManager) {
        spriteBuffer.destroy()
        sprite.recycle()
    }

    override fun update(dt: Float) {
        if (tree.status == Node.Status.Running) {
            tree.execute(dt)
        }

        bindFrameBuffer()
        glViewport(0, 0, width, height)
        glScissor(0, 0, width, height)

        spriteBuffer.use()
        spriteBuffer.start()
        spriteBuffer.putSprite(sprite)
        spriteBuffer.end()

        spriteProgram.use()
        spriteProgram.setMatrix(matrix)
        spriteProgram.setSpriteBuffer(spriteBuffer)
        spriteProgram.draw()
    }

    override fun isFinished(): Boolean {
        return tree.status == Node.Status.Success
    }
}

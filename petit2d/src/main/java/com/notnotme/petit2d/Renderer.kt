package com.notnotme.petit2d

import android.content.Context
import android.opengl.GLES32.GL_BACK
import android.opengl.GLES32.GL_BLEND
import android.opengl.GLES32.GL_COLOR_BUFFER_BIT
import android.opengl.GLES32.GL_CULL_FACE
import android.opengl.GLES32.GL_DEPTH_TEST
import android.opengl.GLES32.GL_FRAMEBUFFER
import android.opengl.GLES32.GL_ONE_MINUS_SRC_ALPHA
import android.opengl.GLES32.GL_SCISSOR_TEST
import android.opengl.GLES32.GL_SRC_ALPHA
import android.opengl.GLES32.GL_TEXTURE0
import android.opengl.GLES32.GL_TEXTURE_2D
import android.opengl.GLES32.glActiveTexture
import android.opengl.GLES32.glBindFramebuffer
import android.opengl.GLES32.glBindTexture
import android.opengl.GLES32.glBlendFunc
import android.opengl.GLES32.glClear
import android.opengl.GLES32.glClearColor
import android.opengl.GLES32.glCullFace
import android.opengl.GLES32.glDisable
import android.opengl.GLES32.glEnable
import android.opengl.GLES32.glScissor
import android.opengl.GLES32.glViewport
import android.opengl.GLSurfaceView
import com.notnotme.petit2d.asset.Texture
import com.notnotme.petit2d.scene.SceneChange
import com.notnotme.petit2d.scene.SceneChanger
import com.notnotme.petit2d.scene.TransitionHolder
import com.notnotme.petit2d.scene.base.BaseScene
import com.notnotme.petit2d.scene.base.BaseTransition
import java.util.Optional
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.ceil
import kotlin.math.max


class Renderer(
    context                         : Context,
    private val gameWidth           : Int,
    private val gameHeight          : Int,
    private val onCreate            : (assets: AssetManager) -> Unit,
    private val sceneFactory        : (id: Int, width: Int, height: Int) -> BaseScene,
    private val transitionFactory   : (id: Int, width: Int, height: Int, outTexture: Texture, inTexture: Texture) -> BaseTransition
) : GLSurfaceView.Renderer, SceneChanger {
    private val assets              : AssetManager                  = AssetManager(context)
    private var width               : Int                           = 0
    private var height              : Int                           = 0
    private var nextScene           : Optional<SceneChange>         = Optional.empty()
    private var transitionHolder    : Optional<TransitionHolder>    = Optional.empty()
    private var time                : Long                          = System.currentTimeMillis()
    private lateinit var scene      : BaseScene

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glDisable(GL_DEPTH_TEST)
        glEnable(GL_SCISSOR_TEST)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        assets.create()

        onCreate(assets)

        scene = sceneFactory(0, gameWidth, gameHeight)
        scene.create()
        scene.enter(assets, this)
    }

    override fun onSurfaceChanged(gl: GL10?, surfaceWidth: Int, surfaceHeight: Int) {
        width = surfaceWidth
        height = surfaceHeight
    }

    override fun onDrawFrame(gl: GL10?) {
        val dt = System.currentTimeMillis().let { now ->
            val previousTime = time
            time = now
            (now - previousTime) / 1000.0f
        }

        nextScene.ifPresent {
            val newScene = sceneFactory(it.sceneId, gameWidth, gameHeight)
            newScene.create()
            newScene.enter(assets, this)

            val transition = transitionFactory(
                it.transitionId,
                gameWidth,
                gameHeight,
                Texture(scene.texture),
                Texture(newScene.texture)
            )

            transition.create()
            transition.enter(assets)

            transitionHolder = Optional.of(
                TransitionHolder(newScene, transition)
            )

            nextScene = Optional.empty()
        }

        transitionHolder.ifPresentOrElse({ holder ->
            val transition = holder.transition
            val inScene = holder.inScene

            if (transition.isFinished()) {
                scene.exit(assets)
                scene.destroy()

                transition.exit(assets)
                transition.destroy()
                transitionHolder = Optional.empty()

                scene = inScene
                scene.update(dt)
            } else {
                scene.update(/*dt*/0.0f)
                inScene.update(/*dt*/0.0f)
                transition.update(dt)
            }
        }, {
            scene.update(dt)
        })

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glViewport(0, 0, width, height)
        glScissor(0, 0, width, height)
        glClearColor(0.16f, 0.16f, 0.16f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)

        val ratioWidth = ceil(max(1, width / gameWidth).toFloat())
        val ratioHeight = ceil(max(1, width / gameHeight).toFloat())

        val ratio = if (ratioWidth > ratioHeight) {
            ratioHeight
        } else {
            ratioWidth
        }.toInt()

        glActiveTexture(GL_TEXTURE0)
        transitionHolder.ifPresentOrElse({
            glBindTexture(GL_TEXTURE_2D, it.transition.texture)
        }, {
            glBindTexture(GL_TEXTURE_2D, scene.texture)
        })

        val quadProgram = assets.quadProgram
        quadProgram.use()
        quadProgram.setScreenSize(width, height)
        quadProgram.setQuadSize(gameWidth * ratio, gameHeight * ratio)
        quadProgram.setTint(1.0f, 1.0f, 1.0f, 1.0f)
        quadProgram.draw()
    }

    override fun change(transitionId: Int, sceneId: Int) {
        if (nextScene.isPresent && transitionHolder.isPresent) {
            throw IllegalStateException("SceneTransition already in place")
        }

        nextScene = Optional.of(
            SceneChange(transitionId, sceneId)
        )
    }
}

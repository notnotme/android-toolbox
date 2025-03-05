package com.notnotme.petit2d

import android.opengl.GLES32.GL_FRAGMENT_SHADER
import android.opengl.GLES32.GL_TRIANGLE_STRIP
import android.opengl.GLES32.GL_VERTEX_SHADER
import android.opengl.GLES32.glAttachShader
import android.opengl.GLES32.glBindVertexArray
import android.opengl.GLES32.glCreateProgram
import android.opengl.GLES32.glDeleteProgram
import android.opengl.GLES32.glDeleteShader
import android.opengl.GLES32.glDrawArrays
import android.opengl.GLES32.glGetUniformLocation
import android.opengl.GLES32.glLinkProgram
import android.opengl.GLES32.glProgramUniform1i
import android.opengl.GLES32.glUniform2i
import android.opengl.GLES32.glUniform4f
import android.opengl.GLES32.glUseProgram
import com.notnotme.petit2d.glkt.checkProgram
import com.notnotme.petit2d.glkt.compileShader
import com.notnotme.petit2d.glkt.createVertexArray
import com.notnotme.petit2d.glkt.deleteVertexArray


class QuadProgram {
    private var vertexArrayId       : Int   = 0
    private var programId           : Int   = 0
    private var screenSizeUniform   : Int   = -1
    private var quadSizeUniform     : Int   = -1
    private var tintUniform         : Int   = -1

    internal fun create() {
        val vertexShader = compileShader(GL_VERTEX_SHADER, VERTEX_SRC)
        val fragmentShader = compileShader(GL_FRAGMENT_SHADER, FRAGMENT_SRC)

        programId = glCreateProgram()
        glAttachShader(programId, vertexShader)
        glAttachShader(programId, fragmentShader)
        glLinkProgram(programId)
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
        checkProgram(programId)

        val textureSlot0 = glGetUniformLocation(programId, "texture_slot_0")
        glProgramUniform1i(programId, textureSlot0, 0)

        screenSizeUniform = glGetUniformLocation(programId, "u_screen_size")
        quadSizeUniform = glGetUniformLocation(programId, "u_quad_size")
        tintUniform = glGetUniformLocation(programId, "u_tint")

        vertexArrayId = createVertexArray()
    }

    internal fun destroy() {
        deleteVertexArray(vertexArrayId)
        vertexArrayId = 0

        glDeleteProgram(programId)
        programId = 0

        screenSizeUniform = -1
        quadSizeUniform = -1
        tintUniform = -1
    }

    fun use() {
        glUseProgram(programId)
        glBindVertexArray(vertexArrayId)
    }

    fun setScreenSize(width: Int, height: Int) {
        glUniform2i(screenSizeUniform, width, height)
    }

    fun setQuadSize(width: Int, height: Int) {
        glUniform2i(quadSizeUniform, width, height)
    }

    fun setTint(r: Float, g: Float, b: Float, a: Float) {
        glUniform4f(tintUniform, r, g, b, a)
    }

    fun draw() {
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
    }

    companion object {
        const val VERTEX_SRC = """#version 300 es
            precision lowp float;
    
            uniform ivec2 u_screen_size;
            uniform ivec2 u_quad_size;
            uniform vec4 u_tint;
    
            out vec2 v_texture;
            out vec4 v_tint;

            const vec2 tlut[4] = vec2[4](
                vec2(0.0, 0.0),
                vec2(1.0, 0.0),
                vec2(0.0, 1.0),
                vec2(1.0, 1.0)
            );
            
            void main() {
                vec2 size = vec2(u_quad_size) / vec2(u_screen_size);
                vec2 plut[4] = vec2[4](
                    vec2(-size.x, -size.y),
                    vec2( size.x, -size.y),
                    vec2(-size.x,  size.y),
                    vec2( size.x,  size.y)
                );
    
                v_texture = tlut[gl_VertexID];
                v_tint = u_tint;
                gl_Position = vec4(plut[gl_VertexID], 0.0, 1.0);
            }
        """

        const val FRAGMENT_SRC = """#version 300 es
            precision lowp float;
    
            in vec2 v_texture;
            in vec4 v_tint;
            out vec4 o_color;
    
            uniform sampler2D texture_slot_0;
    
            void main() {
                o_color = v_tint * texture(texture_slot_0, v_texture);
            }
        """
    }
}

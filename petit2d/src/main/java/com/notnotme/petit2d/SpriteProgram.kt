package com.notnotme.petit2d

import android.opengl.GLES32
import android.opengl.GLES32.GL_FLOAT
import android.opengl.GLES32.GL_FRAGMENT_SHADER
import android.opengl.GLES32.GL_SHORT
import android.opengl.GLES32.GL_UNSIGNED_BYTE
import android.opengl.GLES32.GL_VERTEX_SHADER
import android.opengl.GLES32.glAttachShader
import android.opengl.GLES32.glBindVertexArray
import android.opengl.GLES32.glBindVertexBuffer
import android.opengl.GLES32.glCreateProgram
import android.opengl.GLES32.glDeleteProgram
import android.opengl.GLES32.glDeleteShader
import android.opengl.GLES32.glDrawArraysInstanced
import android.opengl.GLES32.glEnableVertexAttribArray
import android.opengl.GLES32.glGetUniformLocation
import android.opengl.GLES32.glLinkProgram
import android.opengl.GLES32.glProgramUniform1i
import android.opengl.GLES32.glUniformMatrix4fv
import android.opengl.GLES32.glUseProgram
import android.opengl.GLES32.glVertexAttribBinding
import android.opengl.GLES32.glVertexAttribFormat
import android.opengl.GLES32.glVertexBindingDivisor
import com.notnotme.petit2d.glkt.checkProgram
import com.notnotme.petit2d.glkt.compileShader
import com.notnotme.petit2d.glkt.createVertexArray
import com.notnotme.petit2d.glkt.deleteVertexArray


class SpriteProgram {
    private var vertexArrayId   : Int           = 0
    private var programId       : Int           = 0
    private var matrixUniform   : Int           = -1
    private var currentBuffer   : SpriteBuffer? = null

    internal fun create() {
        setupShader()
        setupVertexArray()
    }

    internal fun destroy() {
        currentBuffer = null

        deleteVertexArray(vertexArrayId)
        vertexArrayId = 0

        glDeleteProgram(programId)
        programId = 0
    }

    fun use() {
        glUseProgram(programId)
        glBindVertexArray(vertexArrayId)
    }

    fun setMatrix(matrix: FloatArray) {
        glUniformMatrix4fv(matrixUniform, 1, false, matrix, 0)
    }

    fun setSpriteBuffer(buffer: SpriteBuffer) {
        currentBuffer = buffer
        glBindVertexBuffer(0, buffer.id, 0, STRIDE_SIZE)
    }

    private fun setupShader() {
        val vertexShader = compileShader(GL_VERTEX_SHADER, VERTEX_SRC)
        val fragmentShader = compileShader(GL_FRAGMENT_SHADER, FRAGMENT_SRC)

        programId = glCreateProgram()
        glAttachShader(programId, vertexShader)
        glAttachShader(programId, fragmentShader)
        glLinkProgram(programId)
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
        checkProgram(programId)

        matrixUniform = glGetUniformLocation(programId, "u_matrix")

        val textureSlot0 = glGetUniformLocation(programId, "texture_slot_0")
        val textureSlot1 = glGetUniformLocation(programId, "texture_slot_1")
        val textureSlot2 = glGetUniformLocation(programId, "texture_slot_2")
        val textureSlot3 = glGetUniformLocation(programId, "texture_slot_3")
        val textureSlot4 = glGetUniformLocation(programId, "texture_slot_4")
        val textureSlot5 = glGetUniformLocation(programId, "texture_slot_5")
        val textureSlot6 = glGetUniformLocation(programId, "texture_slot_6")
        val textureSlot7 = glGetUniformLocation(programId, "texture_slot_7")

        glProgramUniform1i(programId, textureSlot0, 0)
        glProgramUniform1i(programId, textureSlot1, 1)
        glProgramUniform1i(programId, textureSlot2, 2)
        glProgramUniform1i(programId, textureSlot3, 3)
        glProgramUniform1i(programId, textureSlot4, 4)
        glProgramUniform1i(programId, textureSlot5, 5)
        glProgramUniform1i(programId, textureSlot6, 6)
        glProgramUniform1i(programId, textureSlot7, 7)
    }

    private fun setupVertexArray() {
        vertexArrayId = createVertexArray()
        glBindVertexArray(vertexArrayId)

        glEnableVertexAttribArray(0)
        glVertexAttribBinding(0, 0)
        glVertexAttribFormat(0, 2, GL_FLOAT, false, POSITION_OFFSET)
        glVertexBindingDivisor(0, 1)

        glEnableVertexAttribArray(1)
        glVertexAttribBinding(1, 0)
        glVertexAttribFormat(1, 1, GL_FLOAT, false, ROTATION_OFFSET)
        glVertexBindingDivisor(1, 1)

        glEnableVertexAttribArray(2)
        glVertexAttribBinding(2, 0)
        glVertexAttribFormat(2, 4, GL_SHORT, true, TEXTURE_OFFSET)
        glVertexBindingDivisor(2, 1)

        glEnableVertexAttribArray(3)
        glVertexAttribBinding(3, 0)
        glVertexAttribFormat(3, 2, GL_SHORT, false, SIZE_OFFSET)
        glVertexBindingDivisor(3, 1)

        glEnableVertexAttribArray(4)
        glVertexAttribBinding(4, 0)
        glVertexAttribFormat(4, 4, GL_UNSIGNED_BYTE, true, TINT_OFFSET)
        glVertexBindingDivisor(4, 1)

        glEnableVertexAttribArray(5)
        glVertexAttribBinding(5, 0)
        glVertexAttribFormat(5, 1, GL_UNSIGNED_BYTE, false, UNIT_OFFSET)
        glVertexBindingDivisor(5, 1)
    }

    fun draw() {
        glDrawArraysInstanced(GLES32.GL_TRIANGLE_STRIP, 0, 4, currentBuffer?.index ?: 0)
    }

    companion object {
        const val VERTEX_SRC = """#version 300 es
            precision lowp float;
        
            layout (location = 0) in vec2 a_translation;
            layout (location = 1) in float a_rotation;
            layout (location = 2) in vec4 a_texture;
            layout (location = 3) in vec2 a_size;
            layout (location = 4) in vec4 a_tint;
            layout (location = 5) in float a_slot;
            uniform mat4 u_matrix;
        
            out vec4 v_tint;
            out vec2 v_texture;
            flat out int v_slot;

            const ivec2 tlut[4] = ivec2[4](
                ivec2(2, 1),
                ivec2(0, 1),
                ivec2(2, 3),
                ivec2(0, 3)
            );
                
            const vec2 plut[4] = vec2[4](
                vec2( 0.5, -0.5),
                vec2(-0.5, -0.5),
                vec2( 0.5,  0.5),
                vec2(-0.5,  0.5)
            );
                
            void main() {
                mat3 rotate_mat = mat3(
                    cos(a_rotation), -sin(a_rotation), 0.0,
                    sin(a_rotation), cos(a_rotation), 0.0,
                    0.0, 0.0, 1.0
                );
        
                mat3 translate_mat = mat3(
                    1.0, 0.0, 0.0,
                    0.0, 1.0, 0.0,
                    a_translation.x, a_translation.y, 0.0
                );
        
                vec3 transformed = translate_mat * rotate_mat * vec3(plut[gl_VertexID] * a_size, 1.0);
                gl_Position = u_matrix * vec4(transformed, 1.0);
        
                v_texture = vec2(a_texture[tlut[gl_VertexID].x], a_texture[tlut[gl_VertexID].y]);
                v_tint = a_tint;
                v_slot = int(a_slot);
            }
        """

        const val FRAGMENT_SRC = """#version 300 es
            precision lowp float;
        
            in vec4 v_tint;
            in vec2 v_texture;
            flat in int v_slot;
        
            out vec4 o_color;
        
            uniform sampler2D texture_slot_0;
            uniform sampler2D texture_slot_1;
            uniform sampler2D texture_slot_2;
            uniform sampler2D texture_slot_3;
            uniform sampler2D texture_slot_4;
            uniform sampler2D texture_slot_5;
            uniform sampler2D texture_slot_6;
            uniform sampler2D texture_slot_7;
        
            void main() {
                float texel_alpha = 0.0;
                switch (v_slot) {
                case 0: o_color = v_tint * texture(texture_slot_0, v_texture); break;
                case 1: o_color = v_tint * texture(texture_slot_1, v_texture); break;
                case 2: o_color = v_tint * texture(texture_slot_2, v_texture); break;
                case 3: o_color = v_tint * texture(texture_slot_3, v_texture); break;
                case 4: o_color = v_tint * texture(texture_slot_4, v_texture); break;
                case 5: o_color = v_tint * texture(texture_slot_5, v_texture); break;
                case 6:
                    texel_alpha = texture(texture_slot_6, v_texture).a * v_tint.a;
                    o_color = vec4(v_tint.rgb, texel_alpha);
                break;
                case 7:
                    texel_alpha = texture(texture_slot_7, v_texture).a * v_tint.a;
                    o_color = vec4(v_tint.rgb, texel_alpha);
                break;
                default: o_color = v_tint;
                }
            }
        """

        const val POSITION_OFFSET   = 0
        const val ROTATION_OFFSET   = 8
        const val TEXTURE_OFFSET    = 12
        const val SIZE_OFFSET       = 20
        const val TINT_OFFSET       = 24
        const val UNIT_OFFSET       = 28
        const val STRIDE_SIZE       = 32
    }
}

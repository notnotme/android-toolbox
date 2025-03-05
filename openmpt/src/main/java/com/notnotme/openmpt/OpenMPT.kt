package com.notnotme.openmpt

object OpenMPT {
    init {
        System.loadLibrary("OpenMPT")
    }

    external fun load(data: ByteArray): Boolean
    external fun close()
    external fun decode(buffer: FloatArray, freq: Int)
    external fun getPattern(): Int
    external fun getRow(): Int
}

package com.notnotme.otom4t


interface Node {
    sealed class Status {
        data object   Running                                       : Status()
        data object   Success                                       : Status()
        data class    Failure(val node: Node, val message: String)  : Status()
    }

    fun execute(dt: Float): Status
}

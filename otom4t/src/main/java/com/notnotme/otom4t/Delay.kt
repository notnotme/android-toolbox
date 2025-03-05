package com.notnotme.otom4t


class Delay(
    private var delay: Float
) : Node {
    override fun execute(dt: Float): Node.Status {
        return if (delay <= 0) {
            Node.Status.Success
        } else {
            delay -= dt
            Node.Status.Running
        }
    }
}

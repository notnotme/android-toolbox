package com.notnotme.otom4t


class ParallelNode(
    private vararg val children: Node
) : Node {
    private var currentStatus   : Node.Status   = Node.Status.Running
    val status                  get()           = currentStatus

    override fun execute(dt: Float): Node.Status {
        if (currentStatus != Node.Status.Running) {
            return currentStatus
        }

        var success = 0
        for (child in children) {
            val status = child.execute(dt)
            if (status is Node.Status.Failure) {
                currentStatus = status
                break
            } else if (status is Node.Status.Success) {
                success += 1
            }
        }

        if (success == children.size) {
            currentStatus = Node.Status.Success
        }

        return currentStatus
    }
}

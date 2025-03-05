package com.notnotme.otom4t


class SequenceNode(
    private vararg val children: Node
) : Node {
    private var currentStatus   : Node.Status   = Node.Status.Running
    private var currentChild    : Int           = 0
    val status                  get()           = currentStatus

    override fun execute(dt: Float): Node.Status {
        if (currentStatus != Node.Status.Running) {
            return currentStatus
        }

        val childStatus = children[currentChild].execute(dt)
        if (childStatus is Node.Status.Failure) {
            currentStatus = childStatus
        } else if (childStatus is Node.Status.Success) {
            currentChild += 1
            currentStatus = if (currentChild == children.size) {
                Node.Status.Success
            } else {
                Node.Status.Running
            }
        }

        return currentStatus
    }
}

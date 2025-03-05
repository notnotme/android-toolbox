package com.notnotme.petit2d.otom4t

import com.notnotme.otom4t.Node
import com.notnotme.petit2d.asset.Font


class BindFont (
    private val fontProvider    : () -> Font,
    private val unit            : Int
) : Node {
    override fun execute(dt: Float): Node.Status {
        fontProvider().bind(unit)
        return  Node.Status.Success
    }
}

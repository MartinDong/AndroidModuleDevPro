package com.dong.lib.common.animator.path

/**
 * <p>视图运动的点</P>
 * Created by Kotlin on 2018/3/6.
 */
class PathPoint {

    companion object {
        //直接移动到指定位置
        val MOVE = 0
        //直线运动
        val LINE = 1
        //曲线运动
        val CURVE = 2

        fun moveTo(x: Float, y: Float): PathPoint {
            return PathPoint(MOVE, x, y)
        }

        fun lintTo(x: Float, y: Float): PathPoint {
            return PathPoint(LINE, x, y)
        }

        fun curveTo(c0x: Float, c0y: Float,
                    c1x: Float, c1y: Float,
                    x: Float, y: Float): PathPoint {
            return PathPoint(c0x, c0y, c1x, c1y, x, y)
        }

    }

    //操作指令
    var mOperation: Int = MOVE

    var mX: Float = 0.0f
    var mY: Float = 0.0f

    var mControl0X: Float = 0.0f
    var mControl0Y: Float = 0.0f

    var mControl1X: Float = 0.0f
    var mControl1Y: Float = 0.0f

    /**
     * Line?MOVE
     */
    constructor(operation: Int, x: Float, y: Float) {
        this.mOperation = operation
        this.mX = x
        this.mY = y
    }

    /**
     * Curve曲线运动
     */
    constructor(c0x: Float, c0y: Float,
                c1x: Float, c1y: Float,
                x: Float, y: Float) {
        this.mControl0X = c0x
        this.mControl0Y = c0y
        this.mControl1X = c1x
        this.mControl1Y = c1y

        this.mX = x
        this.mY = y

        this.mOperation = CURVE
    }

}
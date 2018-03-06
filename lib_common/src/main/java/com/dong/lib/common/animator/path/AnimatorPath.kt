package com.dong.lib.common.animator.path

/**
 * <p>记录一组视图运动的关键点</P>
 * Created by Kotlin on 2018/3/6.
 */
class AnimatorPath {
    //记录一组动画的运动的关键点
    var mPoint: MutableList<PathPoint> = mutableListOf()

    fun moveTo(x: Float, y: Float):PathPoint {
//        mPoint.add(PathPoint.moveTo(x, y))
        return PathPoint.moveTo(x, y)
    }

    fun lintTo(x: Float, y: Float) {
        mPoint.add(PathPoint.lintTo(x, y))
    }

    fun curveTo(c0x: Float, c0y: Float,
                c1x: Float, c1y: Float,
                x: Float, y: Float) :PathPoint{
//        mPoint.add(PathPoint.curveTo(c0x, c0y, c1x, c1y, x, y))
        return PathPoint.curveTo(c0x, c0y, c1x, c1y, x, y)
    }

    //获取运动轨迹关键点
    fun getPoints(): Collection<PathPoint> {
        return mPoint
    }
}
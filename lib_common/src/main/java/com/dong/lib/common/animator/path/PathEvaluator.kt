package com.dong.lib.common.animator.path

import android.animation.TypeEvaluator

/**
 * <p>实现自定义的贝塞尔曲线的差值适配器</P>
 * Created by Kotlin on 2018/3/6.
 */
class PathEvaluator : TypeEvaluator<PathPoint> {
    /**
     * 返回估值
     */
    override fun evaluate(fraction: Float, startValue: PathPoint, endValue: PathPoint): PathPoint {

        var x = 0f
        var y = 0f

        when (endValue.mOperation) {
        //直接移动
            PathPoint.MOVE -> {
                x = endValue.mX
                y = endValue.mY
            }
        //x=起点+fraction*起始点和终点的距离
            PathPoint.LINE -> {
                x = startValue.mX + fraction * (endValue.mX - startValue.mX)
                y = startValue.mY + fraction * (endValue.mY - startValue.mY)
            }
        //三阶贝塞尔曲线
            PathPoint.CURVE -> {
                val oneMinusT = 1 - fraction
                x = oneMinusT * oneMinusT * oneMinusT * startValue.mX +
                        3 * oneMinusT * oneMinusT * fraction * endValue.mControl0X +
                        3 * oneMinusT * fraction * fraction * endValue.mControl1X +
                        fraction * fraction * fraction * endValue.mX


                y = oneMinusT * oneMinusT * oneMinusT * startValue.mY +
                        3 * oneMinusT * oneMinusT * fraction * endValue.mControl0Y +
                        3 * oneMinusT * fraction * fraction * endValue.mControl1Y +
                        fraction * fraction * fraction * endValue.mY
            }
        }

        return PathPoint.moveTo(x, y)
    }
}
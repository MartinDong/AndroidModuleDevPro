package com.dong.module.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.KeyEvent
import android.view.animation.AccelerateInterpolator
import com.alibaba.android.arouter.launcher.ARouter
import com.dong.lib.common.animator.path.AnimatorPath
import com.dong.lib.common.animator.path.PathEvaluator
import com.dong.lib.common.animator.path.PathPoint
import com.dong.lib.common.base.BaseActivity
import com.dong.lib.common.di.component.AppComponent
import com.dong.lib.common.mvp.IPresenter
import com.dong.lib.common.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*

/**
 *  Created by Kotlin on 2018/2/26.
 */
class MainActivity<P : IPresenter> : BaseActivity<P>() {

    //记录按返回键时间
    private var mExitTime: Long = 0

    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {

        btn_news.setOnClickListener {
            //跳转到新闻主页
            ARouter.getInstance().build("/news/center").navigation()
        }

        btn_sqlite.setOnClickListener {
            //跳转到新闻主页
            ARouter.getInstance().build("/sqlite/center").navigation()
        }

        fab_move.setOnClickListener {
            val animatorPath = AnimatorPath()

            animatorPath.moveTo(0f, 0f)

            animatorPath.curveTo(-200f, 200f, -400f, 100f, -600f, 50f)

            //执行属性动画
            val anim = ObjectAnimator.ofObject(
                    //属性动画作用域
                    this,
                    //要反射执行的方法名称
                    "fabMove",
                    //估值器，产生连续的点，根据下面的关键点自动生成
                    PathEvaluator(),
                    //设置关键的点
                    //animatorPath.getPoints().toTypedArray()
                    animatorPath.moveTo(0f, 0f),
                    animatorPath.curveTo(-200f, 200f, -400f, 100f, -600f, 50f)
            )
            anim.interpolator = AccelerateInterpolator()
            anim.duration = 1000
            anim.start()
        }
    }

    fun setFabMove(path: PathPoint) {
        fab_move.translationX = path.mX
        fab_move.translationY = path.mY
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            //两秒之内按返回键就会退出
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtils.showShortToast(getString(R.string.app_exit_hint))
                mExitTime = System.currentTimeMillis()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}

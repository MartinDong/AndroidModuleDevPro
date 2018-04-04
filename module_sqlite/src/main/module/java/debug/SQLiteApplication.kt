package debug

import com.alibaba.android.arouter.launcher.ARouter
import com.dong.lib.common.base.BaseApplication
import com.dong.lib.common.utils.Utils

/**
 * <p>调试当前模块儿使用</p>
 *
 * Created by Kotlin on 2018/2/25.
 */
class SQLiteApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        if (Utils.isAppDebug()) {// 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openDebug()// 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openLog()// 打印日志
        }
        ARouter.init(this)// 尽可能早，推荐在Application中初始化
    }
}
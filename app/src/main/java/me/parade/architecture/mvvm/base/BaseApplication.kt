package me.parade.architecture.mvvm.base

import androidx.multidex.MultiDexApplication
import me.parade.architecture.mvvm.util.lifecycle.KtxLifeCycleCallBack

/**
 * @author : parade
 * date : 2020/9/4
 * description :
 */
class BaseApplication:MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(KtxLifeCycleCallBack())
    }
}
package me.parade.architecture.mvvm.util.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * @author : parade
 * date : 2020/11/2
 * description :
 */
class ProcessLifecycleObserver:DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)//只会调用一次
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)//进入前台
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)//进入后台

    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)//不会调用
    }
}
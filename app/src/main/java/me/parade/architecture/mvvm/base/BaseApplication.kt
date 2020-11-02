package me.parade.architecture.mvvm.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import me.parade.architecture.mvvm.util.lifecycle.KtxLifeCycleCallBack
import me.parade.architecture.mvvm.util.lifecycle.ProcessLifecycleObserver

/**
 * @author : parade
 * date : 2020/9/4
 * description :
 */
class BaseApplication:MultiDexApplication() {

    private val  processLifecycleObserver by lazy { ProcessLifecycleObserver() }

    companion object{
        private lateinit var context:Application
        fun applicationContext():Context = context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        registerActivityLifecycleCallbacks(KtxLifeCycleCallBack())//监听activity创建
        ProcessLifecycleOwner.get().lifecycle.addObserver(processLifecycleObserver)//监听应用进入前后台

    }

    //另外一种获取全局context的方法
    /*init {
        instance = this
    }

    companion object {
        private var instance: BaseApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }*/
}
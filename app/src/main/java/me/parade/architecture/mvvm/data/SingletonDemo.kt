package me.parade.architecture.mvvm.data

/**
 * @author : parade
 * date : 2020/11/2
 * description :单例demo
 */
class SingletonDemo private constructor() {
    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { SingletonDemo() }
    }
}
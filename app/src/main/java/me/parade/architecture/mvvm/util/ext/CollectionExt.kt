package me.parade.architecture.mvvm.util.ext

/**
 * @author : parade
 * date : 2020/9/20
 * description :集合相关扩展
 */

/** 集合去重 */
fun<T> Collection<T>.removeDuplicate():List<T>{
    val list = mutableListOf<T>()
    val set = mutableSetOf<T>()
    this.forEach {
        if (set.add(it)){
            list.add(it)
        }
    }
    return list
}
package me.parade.architecture.mvvm.util.lifecycle

import android.app.Activity
import java.util.*

/**
 * @author : parade
 * date : 2020/9/4
 * description : Activity管理类
 */
object ActivityLifecycleManager {
    private val mActivityList = LinkedList<Activity>()

    val currentActivity: Activity?
        get() =
            if (mActivityList.isEmpty()) null
            else mActivityList.last


    /**
     * push the specified [activity] into the list
     */
    fun pushActivity(activity: Activity) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.add(activity)
            }
        } else {
            mActivityList.add(activity)
        }
    }

    /**
     * pop the specified [activity] into the list
     */
    fun popActivity(activity: Activity) {
        mActivityList.remove(activity)
    }

    fun finishCurrentActivity() {
        currentActivity?.finish()
    }

    fun finishActivity(activity: Activity) {
        mActivityList.remove(activity)
        activity.finish()
    }

    fun  finishActivity(clazz: Class<*>){
        for (activity in mActivityList)
            if (activity.javaClass == clazz)
                activity.finish()
    }

    fun finishAllActivity() {
        for (activity in mActivityList)
            activity.finish()
    }
}
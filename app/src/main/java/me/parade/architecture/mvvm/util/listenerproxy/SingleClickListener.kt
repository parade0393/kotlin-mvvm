package com.jianqi.wuye.util.brvah

import android.view.View

/**
 * @author : parade
 * date : 2021/4/4
 * description : 点击事件代理类
 */
 class SingleClickListener(private val originListener:View.OnClickListener, private val intervalTime:Long = 500):View.OnClickListener {
    var lastClickTime =  0L
    override fun onClick(v: View?) {
        if (System.currentTimeMillis() - lastClickTime >= intervalTime){
            originListener.onClick(v)
            lastClickTime = System.currentTimeMillis()
        }
    }
}
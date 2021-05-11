package com.jianqi.wuye.util.brvah

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener

/**
 * @author : parade
 * date : 2021/3/29
 * description : 万能适配器重复点击代理类
 */
class BRVAHItemClickProxy(private val originItemClickListener: OnItemClickListener, private val intervalTime:Long = 500):OnItemClickListener {
    var lastClickTime =  0L
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
       if (System.currentTimeMillis() - lastClickTime >= intervalTime){
           originItemClickListener.onItemClick(adapter, view, position)
           lastClickTime = System.currentTimeMillis()
       }
    }
}
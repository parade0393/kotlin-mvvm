package me.parade.architecture.mvvm.demo

import me.parade.architecture.mvvm.base.BaseViewModel

/**
 * @author : parade
 * date : 2021/3/21
 * description :
 */
class HomeViewMmodel:BaseViewModel() {
    private val homeReposity by lazy { HomeReposity.getInstance() }

    fun getHomeList(page:Int){
        launchFilterResult(
            block = {
                homeReposity.getHomeList(page)
            },
            success = {
                uiLiveEvent.showToastEvent.postValue(it.datas[0].title)
            }
        )
    }

    fun getCollection(){
        launchFilterResult(
            block = {
                homeReposity.getCollection()
            },
            success = {
                uiLiveEvent.showToastEvent.postValue("踩踩踩")
            }
        )
    }
}
package me.parade.architecture.mvvm.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.parade.architecture.mvvm.base.BaseViewModel
import me.parade.architecture.mvvm.demo.dao.AppDataBase

/**
 * @author : parade
 * date : 2021/3/21
 * description :
 */
class HomeViewMmodel:BaseViewModel() {
    private val homeReposity by lazy { HomeReposity.getInstance(HomeNetWork.getInstance(),
        AppDataBase.getInstance().homeLocaData()) }
    private val _homeListBean = MutableLiveData<HomeListBean>()
    val homeListBean:LiveData<HomeListBean> = _homeListBean


  /*  fun getHomeList(page:Int){
        launchFilterResult(
            block = {
                homeReposity.getHomeList(page)
            },
            success = {
                uiLiveEvent.showToastEvent.postValue(it?.datas?.get(0)?.title)
            }
        )
    }*/

   /* fun getHomeListNew(page: Int){
        launch(
            block = {
                _homeListBean.value = homeReposity.getHomeList(page).data
            }
        )
    }*/

   /* fun getCollection(){
        launchFilterResult(
            block = {
                homeReposity.getCollection()
            },
            success = {
                uiLiveEvent.showToastEvent.postValue("踩踩踩")
            }
        )
    }*/

    fun getHomeList(page:Int,refresh:Boolean){
        launch(
            block = {
                _homeListBean.value = homeReposity.getHomeList(page,refresh)
            }
        )
    }


}
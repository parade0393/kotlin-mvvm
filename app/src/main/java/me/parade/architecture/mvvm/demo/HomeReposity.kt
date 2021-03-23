package me.parade.architecture.mvvm.demo

import me.parade.architecture.mvvm.base.BaseModel
import me.parade.architecture.mvvm.demo.dao.HomeDao

/**
 * @author : parade
 * date : 2021/3/21
 * description :
 */
class HomeReposity private constructor(
    private val netWork: HomeNetWork,
    private val homeDao: HomeDao
) : BaseModel() {

   suspend fun getHomeList(page: Int, refresh: Boolean): HomeListBean? {
        return cacheNetCall(
            {
                netWork.getHomeList(page)
            },{
                homeDao.getHomeList(page)
            },
            {
                if (refresh){
                    homeDao.deleteHomeAll()
                }
                homeDao.insertData(it)
            },{
                !refresh
            }
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: HomeReposity? = null

        fun getInstance(netWork: HomeNetWork, homeDao: HomeDao) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: HomeReposity(netWork, homeDao).also { INSTANCE = it }
        }
    }
}
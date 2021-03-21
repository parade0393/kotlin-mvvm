package me.parade.architecture.mvvm.demo

import me.parade.architecture.mvvm.network.RetrofitClient

/**
 * @author : parade
 * date : 2021/3/21
 * description :
 */
class HomeReposity {
    private val mService by lazy { RetrofitClient.getInstance().create(HomeService::class.java) }

    suspend fun getHomeList(page: Int) = mService.getHomeList(page)
    suspend fun getCollection() = mService.getCollection(280501)

    companion object {
        @Volatile
        private var netWork: HomeReposity? = null

        fun getInstance() = netWork ?: synchronized(this) {
            netWork ?: HomeReposity().also { netWork = it }
        }
    }
}
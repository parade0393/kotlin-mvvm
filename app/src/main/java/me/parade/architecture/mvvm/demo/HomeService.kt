package me.parade.architecture.mvvm.demo

import me.parade.architecture.mvvm.base.BaseResult
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @author : parade
 * date : 2021/3/21
 * description :
 */
interface HomeService {
    /**
     * 项目列表
     * @param page 页码，从0开始
     */
    @GET("article/listproject/{page}/json")
    suspend fun getHomeList(@Path("page") page: Int): BaseResult<HomeListBean>

    @POST("lg/uncollect/{id}/json")
    suspend fun getCollection(@Path("id") id:Int):BaseResult<CollectionBean>


}
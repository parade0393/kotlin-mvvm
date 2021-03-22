package me.parade.architecture.mvvm.base

import com.example.mvvmdemo.basemoudle.base.IBaseResponse
import me.parade.architecture.mvvm.network.ResponseThrowable


abstract class BaseModel {

    /**
     * @param remote 网络数据
     * @param local 本地数据
     * @param save 当网络请求成功后，保存数据等操作
     * @param isUseCache 是否使用缓存
     */
    suspend fun <T> cacheNetCall(
        remote: suspend () -> IBaseResponse<T>,
        local: suspend () -> T?,
        save: suspend (T) -> Unit,
        isUseCache: (T?) -> Boolean = { true }
    ): T? {
        val localData = local.invoke()
        return if (isUseCache(localData) && localData != null) localData
        else {
            val net = remote()
            if (net.isSuccess()){
                return net.data().also { save(it!!) }
            }else{
                throw ResponseThrowable(net)
            }
           /* remote().let { net ->
                if (net.isSuccess()) net.data().also { save(it) }
                throw ResponseThrowable(net)
            }*/
        }
    }

    fun onCleared() {
    }
}
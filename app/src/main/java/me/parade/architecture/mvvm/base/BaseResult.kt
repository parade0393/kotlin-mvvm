package me.parade.architecture.mvvm.base

import com.example.mvvmdemo.basemoudle.base.IBaseResponse

/**
 *   @auther : Aleyn
 *   time   : 2019/11/01
 */
data class BaseResult<T>(
    val errorMsg: String,
    val errorCode: Int,
    val data: T
) : IBaseResponse<T> {

    override fun code() = errorCode

    override fun msg() = errorMsg

    override fun data() = data

    override fun isSuccess() = errorCode == 0

}
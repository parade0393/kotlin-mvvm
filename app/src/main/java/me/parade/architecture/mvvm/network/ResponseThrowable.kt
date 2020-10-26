package me.parade.architecture.mvvm.network

import com.example.mvvmdemo.basemoudle.base.IBaseResponse

/**
 * @author : parade
 * date : 2020 07 2020/7/12
 * description : 异常统一处理
 */
class ResponseThrowable : Exception {
    var errorCode: Int
    var errorMessage: String

    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        errorCode = error.getKey()
        errorMessage = error.getValue()
    }

    constructor(code: Int, msg: String, e: Throwable? = null) : super(e) {
        this.errorCode = code
        this.errorMessage = msg
    }

    constructor(base: IBaseResponse<*>, e: Throwable? = null) : super(e) {
        this.errorCode = base.code()
        this.errorMessage = base.msg()
    }
}
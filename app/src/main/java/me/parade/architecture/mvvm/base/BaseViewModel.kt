package me.parade.architecture.mvvm.base

import androidx.lifecycle.*
import com.example.mvvmdemo.basemoudle.base.IBaseResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.parade.architecture.mvvm.network.ExceptionHandle
import me.parade.architecture.mvvm.network.ResponseThrowable

/**
 * @author : parade
 * date : 2020/7/11
 * description : ViewModel基类
 */
abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    private val _isShowLoadingView = MutableLiveData<Boolean>()
    val isShowLoadingView: LiveData<Boolean>
        get() = _isShowLoadingView

    private val _isShowErrorView = MutableLiveData<Boolean>()
    val isShowErrorView: LiveData<Boolean>
        get() = _isShowErrorView


    val uiLiveEvent by lazy { UILiveEvent() }

    /**
     * 所有的网络请求都在声明周期内执行，页面销毁时自动取消请求(协程)
     */
    private fun launchUI(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch { block() }

    fun <T> launchFlow(block: suspend () -> T): Flow<T> = flow { emit(block()) }


    /**
     * 处理请求逻辑
     *@param block 请求快
     * @param success 成功回调
     * @param error 失败回调
     * @param complete 无论成功或者失败都会回调
     */
    private suspend fun handleException(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                block()
            } catch (throwable: Throwable) {
                error(ExceptionHandle.handleException(throwable))
            } finally {
                complete()
            }
        }
    }

    /**
     * 网络请求 不过滤请求结果
     * @param uiState 默认显示网络加载框，toast接口异常信息 ，可重写uiState改变状态,如果重写了其中一个值，其它的需要的也需要重写
     * @param success 请求成功回调
     * @param error 请求失败回调 默认toast error info
     * @param complete 请求成功和失败都会回调
     */

    fun launch(
        isShowDialog: Boolean = true,
        block: suspend CoroutineScope.() -> Unit,
        error: (suspend CoroutineScope.(ResponseThrowable) -> Unit)? = null,
        complete: suspend CoroutineScope.() -> Unit = {}
    ) {
        if (isShowDialog) {
            uiLiveEvent.showDialogEvent.call()
        }
        launchUI {
            handleException(
                block = withContext(Dispatchers.IO) { block },
                error = {
                    uiLiveEvent.showToastEvent.postValue("${it.errorCode}:${it.errorMessage}")
                    error?.invoke(this, it)

                },
                complete = {
                    uiLiveEvent.dismissDialogEvent.call()
                    complete()
                }
            )
        }
    }


    /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param uiState UI状态 默认显示加载dialog和错误toast
     * @param 成功回调
     * @param error 失败回调
     * @param complete 完成回调（无论成功失败都会调用）
     */
    fun <T> launchFilterResult(
        isShowDialog: Boolean = true,
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: (T?) -> Unit,
        error: (ResponseThrowable) -> Unit = {},
        complete: () -> Unit = {},
    ) {
        if (isShowDialog) {
            uiLiveEvent.showDialogEvent.call()
        }
        launchUI {
            handleException(
                {
                    withContext(Dispatchers.IO) {
                        block().let {
                            if (it.isSuccess()) it.data()
                            else throw ResponseThrowable(it.code(), it.msg())
                        }
                    }.also { success(it) }
                },
                {
                    uiLiveEvent.showToastEvent.postValue("${it.errorCode}:${it.errorMessage}")
                    error(it)
                }, {
                    uiLiveEvent.dismissDialogEvent.call()
                    complete.invoke()
                }
            )
        }

    }


    class UILiveEvent {
        val showDialogEvent by lazy { SingleLiveEvent<Boolean>() }
        val dismissDialogEvent by lazy { SingleLiveEvent<Void>() }
        val showToastEvent by lazy { SingleLiveEvent<String>() }
        val showMsgEvent by lazy { SingleLiveEvent<String>() }
    }
}
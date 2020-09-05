package me.parade.architecture.mvvm.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import me.parade.architecture.mvvm.network.ExceptionHandle
import com.example.mvvmdemo.basemoudle.network.ResponseThrowable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author : parade
 * date : 2020/7/11
 * description : ViewModel基类
 */
abstract class BaseViewModel : ViewModel() {

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
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

    fun <T> launchFlow(block: suspend () -> T): Flow<T> = flow { emit(block()) }


    /**
     * 处理请求逻辑
     *@param block 请求快
     * @param success 成功回调
     * @param error 失败回调
     * @param complete 无论成功或者失败都会回调
     */
    private suspend fun <T> handle(
        block: suspend CoroutineScope.() -> T,
        success: suspend CoroutineScope.(T) -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                success(block())
            } catch (throwable: Throwable) {
                error(ExceptionHandle.handleException(throwable))
            } finally {
                complete()
            }
        }
    }


    fun <T> launch(
        uiState: UIState = UIState(isShowLoadingDialog = true),
        block: suspend CoroutineScope.() -> T,
        success: (suspend CoroutineScope.(T) -> Unit)? = null,
        error: (suspend CoroutineScope.(ResponseThrowable) -> Unit)? = null,
        complete: (suspend CoroutineScope.() -> Unit)? = null
    ) =
        with(uiState) {
            if (isShowLoadingDialog) uiLiveEvent.showDialogEvent.call()
            if (isShowLoadingView) _isShowLoadingView.value = true
            if (isShowErrorView) _isShowErrorView.value = false
            launchUI {
                handle(
                    block = withContext(Dispatchers.IO) { block },
                    success =  { success?.invoke(this, it)  },
                    error = {
                        withContext(Dispatchers.Main) {
                            if (isShowErrorView) _isShowErrorView.value = true
                            if (isShowErrorToast) uiLiveEvent.showToastEvent.postValue("${it.errorCode}:${it.errorMessage}")
                            error?.invoke(this, it)
                        }
                    },
                    complete = {
                            if (isShowLoadingDialog) uiLiveEvent.dismissDialogEvent.call()
                            if (isShowLoadingView) _isShowLoadingView.value = false
                            complete?.invoke(this)
                    }
                )
            }
        }


    class UILiveEvent {
        val showDialogEvent by lazy { SingleLiveEvent<Boolean>() }
        val dismissDialogEvent by lazy { SingleLiveEvent<Boolean>() }
        val showToastEvent by lazy { SingleLiveEvent<String>() }
        val showMsgEvent by lazy { SingleLiveEvent<String>() }
    }
}

/**
 *UI状态
 * @param isShowLoadingDialog 是否现在加载中进度
 * @param isShowLoadingView 是否显示加载中页面
 * @param isShowErrorToast 是否弹出error Toast
 * @param isShowErrorView 是否显示错误页面
 */
data class UIState(
    val isShowLoadingDialog: Boolean = false,
    val isShowLoadingView: Boolean = false,
    val isShowErrorToast: Boolean = false,
    val isShowErrorView: Boolean = false
)
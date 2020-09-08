package com.example.mvvmdemo.basemoudle.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import me.parade.architecture.mvvm.base.BaseViewModel
import me.parade.architecture.mvvm.view.LoadingDialog
import java.lang.reflect.ParameterizedType

/**
 * @author : parade
 * date : 2020 07 2020/7/24
 * description :Fragment基类
 */
abstract class BaseFragment<VM: BaseViewModel,DB:ViewDataBinding>:Fragment() {
    protected lateinit var viewModel: VM

    protected var mBinding: DB? = null


    private  var loadingDialog: LoadingDialog? = null

    //是否第一次加载
     var isFirst: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>

        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)){
            mBinding =  DataBindingUtil.inflate(inflater,getLayoutId(),container,false)
            return mBinding?.root
        }

        return inflater.inflate(getLayoutId(),container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onVisible()
        createViewModel()
        registerDefUIChange()
        initView(savedInstanceState)
        initEvent()
        initObserver()
    }

    /** 初始化试图的一些操作，比如RecyclerView的初始化等 */
    open fun initView(savedInstanceState: Bundle?) {}

    /** 事件监听 */
    open fun initEvent(){}

    /** 数据观察 */
    open fun initObserver(){}

    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType){
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM>?: BaseViewModel::class.java
            val viewModelStore = if (isShareVM()) activity!!.viewModelStore else this.viewModelStore
            viewModel = ViewModelProvider(viewModelStore,ViewModelProvider.NewInstanceFactory()).get(tClass) as VM
        }
    }

    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst){
            isFirst = false
            lazyLoad()
        }
    }
    override fun onResume() {
        super.onResume()
        onVisible()
    }

    private fun registerDefUIChange(){
        viewModel.uiLiveEvent.showDialogEvent.observe(viewLifecycleOwner, {
            showLoading()
        })

        viewModel.uiLiveEvent.dismissDialogEvent.observe(viewLifecycleOwner, {
            dismissDialog()
        })

        viewModel.uiLiveEvent.showToastEvent.observe(viewLifecycleOwner, {
            ToastUtils.showShort(it)
        })

        viewModel.uiLiveEvent.showMsgEvent.observe(viewLifecycleOwner, {
            handleEvent(it)
        })
    }

    /**
     *显示加载框
     */
    private fun showLoading(){
        if (loadingDialog == null){
            loadingDialog = LoadingDialog(requireContext())
        }
        loadingDialog?.showDialog()
    }

    /**
     * 关闭加载框
     */
    private fun dismissDialog(){
        loadingDialog?.hideDialog()
    }

    open fun handleEvent(msg:String){}

    /**
     * 是否和 Activity 共享 ViewModel,默认不共享
     * Fragment 要和宿主 Activity 的泛型是同一个 ViewModel
     */
    open fun isShareVM(): Boolean = false

    /**
     * 加载布局文件
     */
    abstract fun getLayoutId(): Int

    /**
     * 懒加载
     */
    open fun lazyLoad(){}

}
package me.parade.architecture.mvvm.base

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import me.parade.architecture.mvvm.util.StatusBarUtil
import me.parade.architecture.mvvm.view.LoadingDialog
import java.lang.reflect.ParameterizedType

/**
 * @author : parade
 * date : 2020 07 2020/7/24
 * description :activity基类
 */
abstract class BaseActivity<VM: BaseViewModel,DB:ViewDataBinding>:AppCompatActivity() {

    protected lateinit var viewModel:VM
//    protected var mBinding:DB? = null
    protected lateinit var mBinding:DB
    private  var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()
        initViewDataBinding()
        StatusBarUtil.setStatusBarLightMode(this,Color.WHITE)
        initView(savedInstanceState)
        initEvent()
        initData()
    }

    /**
     * setContentView之前调用
     */
    protected fun beforeSetContentView() {

    }


    /**
     * 加载布局文件
     */
    abstract fun getLayoutId():Int

    /**
     * 做一些初始化操作
     */
    open fun initView(savedInstanceState: Bundle?){}
    /** 事件监听 */
    open fun initEvent(){}
    /**
     * 做一些初始化操作
     */
    open fun initData(){}




    /**
     * 初始化ViewModel和DataBinding
     */
    private fun initViewDataBinding() {
        //获得泛型参数DB的实际类型
        val clazz =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != clazz && ViewDataBinding::class.java.isAssignableFrom(clazz)){
            //第二个泛型参数的实际类型不是ViewDataBinding，但是时ViewDataBinding的子类，代表使用了DataBinding
            mBinding = DataBindingUtil.setContentView(this,getLayoutId())
            mBinding?.lifecycleOwner = this
        }else setContentView(getLayoutId())
        createViewModel()
    }


    @Suppress("UNCHECKED_CAST")
    protected fun createViewModel(){
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType){
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(tClass) as VM
        }
    }

    open fun handleEvent(msg:String){}

}
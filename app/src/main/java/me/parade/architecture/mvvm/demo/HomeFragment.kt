package me.parade.architecture.mvvm.demo

import android.os.Bundle
import com.example.mvvmdemo.basemoudle.base.BaseFragment
import com.jianqi.wuye.base.webview.BaseWebViewActivity
import me.parade.architecture.mvvm.R
import me.parade.architecture.mvvm.databinding.FragmentHomeBinding

/**
 * @author : parade
 * date : 2021/3/21
 * description :
 */
class HomeFragment:BaseFragment<HomeViewMmodel,FragmentHomeBinding>() {
    override fun getLayoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.btnTest.setOnClickListener {
            BaseWebViewActivity.actionStart(requireContext(),"http://192.168.3.249:8080/")
        }

    }
}
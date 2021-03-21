package me.parade.architecture.mvvm.demo

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.example.mvvmdemo.basemoudle.base.BaseFragment
import me.parade.architecture.mvvm.R

/**
 * @author : parade
 * date : 2021/3/21
 * description :
 */
class HomeFragment:BaseFragment<HomeViewMmodel,ViewDataBinding>() {
    override fun getLayoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
//        viewModel.getHomeList(0)
        viewModel.getCollection()
    }
}
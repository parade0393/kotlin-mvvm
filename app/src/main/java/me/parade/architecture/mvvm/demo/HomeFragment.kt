package me.parade.architecture.mvvm.demo

import android.os.Bundle
import com.example.mvvmdemo.basemoudle.base.BaseFragment
import me.parade.architecture.mvvm.R
import me.parade.architecture.mvvm.databinding.FragmentHomeBinding
import me.parade.architecture.mvvm.util.ext.clickWithTrigger

/**
 * @author : parade
 * date : 2021/3/21
 * description :
 */
class HomeFragment:BaseFragment<HomeViewMmodel,FragmentHomeBinding>() {
    override fun getLayoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
//        viewModel.getHomeList(0)
        /*viewModel.getHomeList(0,false)
        viewModel.homeListBean.observe(this){bean->
            bean?.let {
                toast(it.datas[0].title?:"无")
            }
        }*/

        mBinding.btnTest.clickWithTrigger({
            println("1111111111111111")
        },again = {
            println("2222222222")
        })

    }
}
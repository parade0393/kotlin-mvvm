package me.parade.architecture.mvvm

import androidx.databinding.ViewDataBinding
import com.example.mvvmdemo.basemoudle.base.NoViewModel
import me.parade.architecture.mvvm.base.BaseActivity

class MainActivity : BaseActivity<NoViewModel,ViewDataBinding>() {

    override fun getLayoutId() = R.layout.activity_main

}
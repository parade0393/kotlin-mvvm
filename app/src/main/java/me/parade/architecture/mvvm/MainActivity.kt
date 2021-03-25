package me.parade.architecture.mvvm

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.example.mvvmdemo.basemoudle.base.NoViewModel
import me.parade.architecture.mvvm.base.BaseActivity

class MainActivity : BaseActivity<NoViewModel,ViewDataBinding>() {

    override fun getLayoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        /*findViewById<Button>(R.id.button).setOnClickListener{
            startKtxActivity<Paging3Activity>()
        }*/
    }

}
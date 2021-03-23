package me.parade.architecture.mvvm

import android.os.Bundle
import android.widget.Button
import androidx.databinding.ViewDataBinding
import com.example.mvvmdemo.basemoudle.base.NoViewModel
import me.parade.architecture.mvvm.base.BaseActivity
import me.parade.architecture.mvvm.demo.pageing3.Paging3Activity
import me.parade.architecture.mvvm.util.ext.startKtxActivity

class MainActivity : BaseActivity<NoViewModel,ViewDataBinding>() {

    override fun getLayoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        findViewById<Button>(R.id.button).setOnClickListener{
            startKtxActivity<Paging3Activity>()
        }
    }

}
package me.parade.architecture.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.setContent("Hello World!是你的请第三方第四次峰会超常发挥成功非常高更充分法定规划佛教护法都是九回复都是恢复的都是废话是非常的身份第三方的第三方的身份和的第三方黄金圣斗士的 的身份和速度放缓速度放缓u")
    }
}
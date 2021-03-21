package me.parade.architecture.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.parade.architecture.mvvm.base.BaseApplication
import me.parade.architecture.mvvm.util.ext.getSpValue
import me.parade.architecture.mvvm.util.ext.putSpValue
import me.parade.architecture.mvvm.util.ext.toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.setContent("Hello World!是你的请第三方第四次峰会超常发挥成功非常高更充分法定规划佛教护法都是九回复都是恢复的都是废话是非常的身份第三方的第三方的身份和的第三方黄金圣斗士的 的身份和速度放缓速度放缓u")

        button.setOnClickListener {

            BaseApplication.applicationContext().putSpValue("int",1)
            toast(BaseApplication.applicationContext().getSpValue("int",0).toString())
        }

        AppDataBase.getInstance().communityDao().insertAll(CommunityEntity("2020-11-12"
        ,"街道",1,2,"洛阳","2024-12","4",0,"1",1,"小区1","小区1"
        ,"2020-12","","","",1,"","",""))
//        AppDataBase.getInstance().communityDao().insertAll(CommunityEntity(1,"xia","2020-11","街道","2020-11-12"))
    }
}
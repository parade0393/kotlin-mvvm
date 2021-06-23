package me.parade.architecture.mvvm.util.ext

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build

/**
 * @author : parade
 * date : 2021/3/3
 * description :
 */

fun Context.dp2px(dpValue: Float):Int{
    val density = resources.displayMetrics.density
    return (dpValue*density+0.5f).toInt()
}

//是否安装了微信
fun Context.isWeixinAvilible():Boolean{
    val installedPackages = packageManager.getInstalledPackages(0)// 获取所有已安装程序的包信息
    installedPackages.forEach {
        if ("com.tencent.mm" == it.packageName){
            return true
        }
    }
    return false
}

/**
 * 是否安装了某个应用
 */
fun Context.isInstallAvailable(packageName: String):Boolean{
    val installedPackages = packageManager.getInstalledPackages(0)// 获取所有已安装程序的包信息
    installedPackages.forEach {
        if (packageName == it.packageName){
            return true
        }
    }
    return false
}

/**
 * 是否安装了支付宝
 */
fun Context.isAliPayInstalled():Boolean{
    val uri = Uri.parse("alipays://platformapi/startApp")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    val resolveActivity = intent.resolveActivity(packageManager)
    return resolveActivity != null
}


/**
 * 检测出网络状态是否可用
 */
fun Context.isNetWorkAvailable():Boolean{
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    connectivityManager?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //6.0以上
            val nw = it.activeNetwork?:return false
            val acNw = it.getNetworkCapabilities(nw)?:return false
            return when{
                /* acNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true//wifi
                acNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true//蜂窝传输？
                acNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true//以太网*/
                acNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) -> true
                else -> false
            }
        }else{
            return  it.activeNetworkInfo?.isConnected?:false
        }
    }
    return false
}
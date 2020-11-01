package me.parade.architecture.mvvm.util.ext

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * @author : parade
 * date : 2020/10/27
 * description :扩展ContextCompat
 */

fun Fragment.getColor(@ColorRes id:Int):Int = ContextCompat.getColor(requireContext(),id)

//activity中有同样签名的方法getColor(@ColorRes id:Int)
fun FragmentActivity.getColorByRes(@ColorRes id:Int):Int =  ContextCompat.getColor(this,id)
package me.parade.architecture.mvvm.util.ext

import android.view.Gravity
import android.widget.Toast
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * @author : parade
 * date : 2020/10/26
 * description :toast扩展
 */

/**
 * @param message toast信息
 * @param duration How long to display the message.  Either Toast.LENGTH_SHORT(默认) or Toast.LENGTH_LONG
 * @param gravity toast位置 由Gravity控制，默认center
 */
fun Fragment.toast(
    message: String,
    @IntRange(from = 0, to = 1) duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.CENTER
){
    val makeText = Toast.makeText(requireContext(), message, duration)
    makeText.setGravity(gravity, 0, 0)
    makeText.show()
}

/**
 * @param message toast信息
 * @param duration How long to display the message.  Either Toast.LENGTH_SHORT(默认) or Toast.LENGTH_LONG
 * @param gravity toast位置 由Gravity控制，默认center
 */
fun FragmentActivity.toast(message: String,
                           @IntRange(from = 0, to = 1) duration: Int = Toast.LENGTH_SHORT,
                           gravity: Int = Gravity.CENTER){
    val makeText = Toast.makeText(this, message, duration)
    makeText.setGravity(gravity, 0, 0)
    makeText.show()
}


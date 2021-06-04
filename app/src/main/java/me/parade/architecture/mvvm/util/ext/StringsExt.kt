package me.parade.architecture.mvvm.util.ext

import android.os.Build
import android.text.Html
import android.widget.TextView
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.File
import java.security.MessageDigest
import java.util.regex.Pattern

/**
 * @author : parade
 * date : 2020 07 2020/7/25
 * description :String 的扩展函数
 */

/** 是否是合法的json字符串 */
fun String.isValidateJson(): Boolean {
    val jsonElement: JsonElement?
    try {
        jsonElement = JsonParser().parse(this)
    } catch (e: Exception) {
        return false
    }

    if (jsonElement == null) {
        return false
    }
    return jsonElement.isJsonArray || jsonElement.isJsonObject || jsonElement.isJsonPrimitive
}

/**
 * 查找指定字符在字符串第n次出现的位置
 * @param letter 指定的字符
 * @param position 第几次出现
 * @return 出现的位置 找不到返回-1
 */
fun String.findIndexOfPosition(letter: String, position: Int): Int {
    val pattern = Pattern.compile(letter)
    val matcher = pattern.matcher(this)
    var num = 0
    while (matcher.find()) {
        num++
        if (num == position) break
    }
    return try {
        matcher.start()
    } catch (e: IllegalStateException) {
        -1
    }
}

/**
 * 一个字符串里某个特定的字符串出现的次数
 * @param letter 要匹配的字符串
 */
fun String.timesOfStr(letter: String):Int{
    val pattern = Pattern.compile(letter)
    val matcher = pattern.matcher(this)
    var times = 0
    while (matcher.find()){
        times++
    }
    return times
}

/**
 * 一个字符串是否只有空格
 */
fun String.isSpace(): Boolean {
    this.forEach {
        if (!Character.isWhitespace(it)) return false
    }
    return true
}

/**
 * 将字符串最后一个数字转成Int
 */
fun String.lastCharToInt() = Character.getNumericValue(this.last())

/**
 * 根据文件名称获取文件类型
 */
fun String.getFileExtension(): String {
    if (isSpace()) return this
    val lastPoi = this.lastIndexOf(".")
    val lastSep = this.lastIndexOf(File.separator)
    if (lastPoi == -1 || lastSep >= lastPoi) return ""
    return this.substring(lastPoi + 1)
}

/**
 * 根据文件全路径获取不带扩展名的文件名
 */
fun String.getFileNameNoExtension(): String {
    if (isSpace()) return this
    val lastPoi = this.lastIndexOf(".")
    val lastSep = this.lastIndexOf(File.separator)
    if (lastSep == -1) {
        return if (lastPoi == -1) this else this.substring(0, lastPoi)
    }

    if (lastPoi == -1 || lastSep > lastPoi) {
        return this.substring(lastSep + 1)
    }

    return this.substring(lastSep + 1, lastPoi)
}

/**
 * 字符串MD5加密
 */
fun String.getMd5String(): String {
    val instance = MessageDigest.getInstance("md5").also { it.update(this.toByteArray()) }
    return instance.digest().joinToString(separator = "") { "%02x".format(it) }
}

/**
 * 从url中获取指定参数值 url 符合以下格式?id=3&type=tre,最前面的有?号
 */
fun String.getParamByUrl(name:String):String?{
    val ac = this.plus("&")
    val regex = "([?&])#?${name}=[a-zA-Z0-9]*(&)"//这个可能合理，如果只有一个参数可能就获取不到了
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(ac)
    return if (matcher.find()) {
        matcher.group(0).split("=")[1].replace("&", "")
    } else null
}
//解析成Html
fun String.parseHtml(textView: TextView) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        textView.setText(Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT))
    } else {
        textView.setText(Html.fromHtml(this))
    }
}

/**
 * 实现手机号中间4位显示*或者切换不显示*
 * @param originalPhone 真实手机号
 */
fun String.handlePhoneMiddleText(originalPhone: String): String {
    return if (contains("*")) originalPhone else this.replace(
        Regex("(\\d{3})\\d{4}(\\d{4})"),
        "$1****$2"
    )
}
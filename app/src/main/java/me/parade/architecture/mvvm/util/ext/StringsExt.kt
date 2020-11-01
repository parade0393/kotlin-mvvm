package me.parade.architecture.mvvm.util.ext

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.File
import java.util.regex.Pattern

/**
 * @author : parade
 * date : 2020 07 2020/7/25
 * description :String 的扩展函数
 */

/** 是否是合法的json字符串 */
fun String.isValidateJson():Boolean{
    val jsonElement: JsonElement?
    try {
        jsonElement = JsonParser().parse(this)
    }catch (e:Exception){
        return false
    }

    if (jsonElement == null){
        return false
    }
    return jsonElement.isJsonArray||jsonElement.isJsonObject||jsonElement.isJsonPrimitive
}

/**
 * 查找指定字符在字符串第n次出现的位置
 * @param letter 指定的字符
 * @param position 第几次出现
 * @return 出现的位置 找不到返回-1
 */
fun String.findIndexOfPosition(letter:String,position:Int):Int{
    val pattern = Pattern.compile(letter)
    val matcher = pattern.matcher(this)
    var num = 0
    while (matcher.find()){
        num++
        if (num == position) break
    }
    return try {
        matcher.start()
    }catch (e:IllegalStateException){
        -1
    }
}

/**
 * 一个字符串是否只有空格
 */
fun String.isSpace():Boolean{
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
fun String.getFileExtension():String{
    if (isSpace()) return this
    val lastPoi = this.lastIndexOf(".")
    val lastSep = this.lastIndexOf(File.separator)
    if (lastPoi == -1 || lastSep>=lastPoi) return ""
    return this.substring(lastPoi+1)
}

/**
 * 根据文件全路径获取不带扩展名的文件名
 */
fun String.getFileNameNoExtension():String{
    if (isSpace()) return this
    val lastPoi = this.lastIndexOf(".")
    val lastSep = this.lastIndexOf(File.separator)
    if (lastSep == -1){
        return if (lastPoi == -1) this else this.substring(0,lastPoi)
    }

    if (lastPoi == -1 || lastSep > lastPoi){
        return this.substring(lastSep+1)
    }

    return this.substring(lastSep+1,lastPoi)
}
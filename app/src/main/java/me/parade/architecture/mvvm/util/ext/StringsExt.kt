package me.parade.architecture.mvvm.util.ext

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.lang.Exception

/**
 * @author : parade
 * date : 2020 07 2020/7/25
 * description :String 的扩展函数
 */
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
    return jsonElement.isJsonObject
}
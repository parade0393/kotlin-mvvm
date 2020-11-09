package me.parade.architecture.mvvm.util.ext

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * @author : parade
 * date : 2020/10/20
 * description :gson工具类
 */

/**
 * Gson泛型生成
 */
inline fun <reified T> genericType(): Type = object: TypeToken<T>() {}.type
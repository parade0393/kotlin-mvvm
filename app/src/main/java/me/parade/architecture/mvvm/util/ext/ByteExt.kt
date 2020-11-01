package me.parade.architecture.mvvm.util.ext

/**
 * @author : parade
 * date : 2020/11/1
 * description :
 */

/**
 * byte数组转16进制字符串
 */
fun ByteArray.toHexString():String = joinToString(separator = "") { "%02x".format(it) }
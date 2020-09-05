package me.parade.architecture.mvvm.network

import java.lang.RuntimeException

/**
 * @author : parade
 * date : 2020 07 2020/7/25
 * description :
 */
data class ResultException( val code:String,override val message:String) : RuntimeException()
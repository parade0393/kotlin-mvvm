package me.parade.architecture.mvvm.demo

/**
 * @author : parade
 * date : 2021/3/21
 * description :
 */
data class HomeListBean (
    val curPage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: MutableList<ArticlesBean>
)
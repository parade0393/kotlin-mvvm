package me.parade.architecture.mvvm.demo

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pcl.mvvm.data.db.converters.ArticlesTypeConverters

/**
 * @author : parade
 * date : 2021/3/21
 * description :
 */
@Entity(tableName = "home_data")
@TypeConverters(ArticlesTypeConverters::class)
data class HomeListBean (
    @PrimaryKey
    val curPage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: MutableList<ArticlesBean>
)
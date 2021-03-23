package me.parade.architecture.mvvm.demo.dao

import androidx.room.*
import me.parade.architecture.mvvm.demo.HomeListBean

/**
 *   @auther : Aleyn
 *   time   : 2020/03/12
 */
@Dao
interface HomeDao {

    @Query("SELECT * FROM HOME_DATA WHERE curPage = :page")
    suspend fun getHomeList(page: Int): HomeListBean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(homeListBean: HomeListBean?)

    @Query("DELETE FROM HOME_DATA")
    suspend fun deleteHomeAll()

    @Update
    suspend fun updataData(homeListBean: HomeListBean)

    @Delete
    suspend fun deleteData(vararg data: HomeListBean)
}
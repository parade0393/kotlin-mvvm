package me.parade.architecture.mvvm.demo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * @author : parade
 * date : 2021/3/20
 * description :
 */
@Dao
interface CommunityDao {
    @Query("select * from CommunityEntity")
    fun getAll():List<CommunityEntity>

    @Insert
    fun insertAll(vararg communityEntity: CommunityEntity)
}
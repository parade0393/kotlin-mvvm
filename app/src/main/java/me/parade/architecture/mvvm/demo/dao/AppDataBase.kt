 package me.parade.architecture.mvvm.demo.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.parade.architecture.mvvm.base.BaseApplication
import me.parade.architecture.mvvm.demo.CommunityDao
import me.parade.architecture.mvvm.demo.HomeListBean

 /**
 * @author : parade
 * date : 2021/3/20
 * description :
 */
@Database(entities = [HomeListBean::class, CommunityEntity::class ], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun communityDao(): CommunityDao
    abstract fun homeLocaData(): HomeDao

    companion object {
        fun getInstance() = SingleHoler.INSTANCE
    }

    private object SingleHoler {
        val INSTANCE = Room.databaseBuilder(
            BaseApplication.applicationContext(),
            AppDataBase::class.java,
            "fu_db"
        ).allowMainThreadQueries()
            .build()
    }
}
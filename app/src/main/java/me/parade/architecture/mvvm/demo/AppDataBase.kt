 package me.parade.architecture.mvvm.demo

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.parade.architecture.mvvm.base.BaseApplication

/**
 * @author : parade
 * date : 2021/3/20
 * description :
 */
@Database(entities = arrayOf(CommunityEntity::class), version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun communityDao(): CommunityDao

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
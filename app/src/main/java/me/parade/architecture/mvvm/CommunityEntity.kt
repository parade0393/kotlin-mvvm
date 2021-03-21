package me.parade.architecture.mvvm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CommunityEntity(
    @ColumnInfo(name = "add_time") val addTime: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo val amount: Int,
    @ColumnInfo(name = "city") val city: Int,
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo val completionTime: String,
    @ColumnInfo val counties: String,
    @ColumnInfo(name = "delete_state") val deleteState: Int,
    @ColumnInfo val description: String,
    @PrimaryKey val id: Int,
    @ColumnInfo val img: String,
    @ColumnInfo(name = "c_name") val name: String,
    @ColumnInfo(name = "opening_time") val openingTime: String,
    @ColumnInfo val other: String,
    @ColumnInfo val principal: String,
    @ColumnInfo val principalPhone: String,
    @ColumnInfo(name = "property_id") val propertyId: Int,
    @ColumnInfo val proportion: String,
    @ColumnInfo val province: String,
    @ColumnInfo  val updateTime: String
)
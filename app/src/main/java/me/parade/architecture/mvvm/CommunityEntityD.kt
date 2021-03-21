package me.parade.architecture.mvvm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CommunityEntityD(

    @ColumnInfo(name = "add_time") val addTime: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "amount") val amount: Any,
    @ColumnInfo(name = "city") val city: Int,
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "completion_time") val completionTime: Any,
    @ColumnInfo(name = "counties") val counties: Any,
    @ColumnInfo(name = "deleteState") val deleteState: Int,
    @ColumnInfo(name = "description") val description: Any,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "img") val img: Any,
    @ColumnInfo(name = "c_name") val name: String,
    @ColumnInfo(name = "opening_time") val openingTime: String,
    @ColumnInfo(name = "other") val other: Any,
    @ColumnInfo(name = "principal") val principal: Any,
    @ColumnInfo(name = "principal_phone") val principalPhone: Any,
    @ColumnInfo(name = "property_id") val propertyId: Int,
    @ColumnInfo(name = "proportion") val proportion: Any,
    @ColumnInfo(name = "province") val province: Any,
    @ColumnInfo(name = "update_time")  val updateTime: Any
)
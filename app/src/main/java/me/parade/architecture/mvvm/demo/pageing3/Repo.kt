package me.parade.architecture.mvvm.demo.pageing3

import com.google.gson.annotations.SerializedName

/**
 * @author : parade
 * date : 2021/3/23
 * description :
 */
data class Repo(
   @SerializedName("id") val id: Int,
   @SerializedName("name") val name:String,
   @SerializedName("description") val description:String?,
   @SerializedName("stargazers_count") val starCount:Int
)
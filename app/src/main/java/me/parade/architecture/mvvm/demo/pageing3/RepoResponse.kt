package me.parade.architecture.mvvm.demo.pageing3

import com.google.gson.annotations.SerializedName

/**
 * @author : parade
 * date : 2021/3/23
 * description :
 */
class RepoResponse {
   @SerializedName("items") val items:List<Repo> = emptyList()
}
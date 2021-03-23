package me.parade.architecture.mvvm.demo.pageing3

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * @author : parade
 * date : 2021/3/23
 * description :
 */
/**
 * 在集成PagingSource的时候需要声明两个泛型，第一个类型表示页数的数据类型，第二个泛型表示每一项数据(不是每一页)
 */
class RepoPagingSource (private val gitHubService: GitHubService):PagingSource<Int,Repo>(){
    override fun getRefreshKey(state: PagingState<Int, Repo>): Int?  = null

    /**
     * params.key->当前的页数，key是可能为null的，如果为null的话，我们就默认将当前页数设置为第一页
     * params.loadSize-> 表示每一页包含多少条数据
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            val page = params.key?:1//首页是1
            val pageSize = params.loadSize//每夜几条
            val repoResponse = gitHubService.searchRepos(page,pageSize)
            val repoItems = repoResponse.items
            //如果当前页已经是第一页或最后一页，那么它的上一页或下一页就为nul
            val preKey = if (page>1) page - 1 else null
            val nextKey = if (repoItems.isNotEmpty()) page + 1 else null
            LoadResult.Page(repoItems,preKey,nextKey)
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}
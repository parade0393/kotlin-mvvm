package me.parade.architecture.mvvm.demo.pageing3

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

/**
 * @author : parade
 * date : 2021/3/23
 * description :
 */
object RepoRepository {
    private const val PAGE_SIZE = 50
    private val gitHubService = GitHubService.create()

    fun getPagingData(): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { RepoPagingSource(gitHubService) }
        ).flow
    }
}
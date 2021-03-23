package me.parade.architecture.mvvm.demo.pageing3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

/**
 * @author : parade
 * date : 2021/3/23
 * description :
 */
class RepoViewModel:ViewModel() {
    //cachedIn()函数，这是用于将服务器返回的数据在viewModelScope这个作用域内进行缓存，假如手机横竖屏发生了旋转导致Activity重新创建，Paging 3就可以直接读取缓存中的数据，而不用重新发起网络请求了
    fun getPagingData(): Flow<PagingData<Repo>> {
        return RepoRepository.getPagingData().cachedIn(viewModelScope)
    }
}
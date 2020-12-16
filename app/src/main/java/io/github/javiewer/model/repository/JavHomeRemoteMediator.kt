package io.github.javiewer.model.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.orhanobut.logger.Logger
import io.github.javiewer.JAViewer
import io.github.javiewer.model.database.AppDatabase
import io.github.javiewer.model.entity.Movie
import io.github.javiewer.model.network.BasicService
import io.github.javiewer.model.network.provider.AVMOProvider
import io.github.javiewer.util.ext.isConnectedNetwork
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class JavHomeRemoteMediator : RemoteMediator<Int, Movie>() {
  /**
   * 首页列表加载逻辑：
   * 1.首次进入一定会进行网络请求
   * 2.如果数据相同就取缓存结果
   *
   */
  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, Movie>
  ): MediatorResult {
    try {
      /**
       * 在这个方法内将会做三件事
       *
       * 1. 参数 LoadType 有个三个值，关于这三个值如何进行判断
       *      LoadType.REFRESH
       *      LoadType.PREPEND
       *      LoadType.APPEND
       *
       * 2. 请问网络数据
       *
       * 3. 将网路插入到本地数据库中
       */

      val movieDao = JAViewer.DB.movieDao()
//            val remoteKeysDao = db.remoteKeysDao()
      // 第一步： 判断 LoadType
      val pageKey = when (loadType) {
        // 首次访问 或者调用 PagingDataAdapter.refresh()
        LoadType.REFRESH -> null

        // 在当前加载的数据集的开头加载数据时
        LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

        LoadType.APPEND -> { // 下来加载更多时触发

          /**
           *
           * 这里主要获取下一页数据的开始位置，可以理解为从什么地方开始加载下一页数据
           * 这里有两种方式来获取下一页加载数据的位置
           * 方式一：这种方式比较简单，当前页面最后一条数据是下一页的开始位置
           * 方式二：比较麻烦，当前分页数据没有对应的远程 key，这个时候需要我们自己建表,
           */

          /**
           * 方式一：这种方式比较简单，当前页面最后一条数据是下一页的开始位置
           * 通过 load 方`法的参数 state 获取当页面最后一条数据
           */
          Logger.d("APPEND")
          val lastItem = state.lastItemOrNull()
              ?: return MediatorResult.Success(
                  endOfPaginationReached = true
              )
          lastItem.page

          /**
           * 方式二：比较麻烦，当前分页数据没有对应的远程 key，这个时候需要我们自己建表
           */
//                    val remoteKey = db.withTransaction {
//                        db.remoteKeysDao().getRemoteKeys(remotePokemon)
//                    }
//                    if (remoteKey == null || remoteKey.nextKey == null) {
//                        return MediatorResult.Success(endOfPaginationReached = true)
//                    }
//                    remoteKey.nextKey
        }
      }

      Logger.d(pageKey)
      if (JAViewer.application.isConnectedNetwork()) {
        // 无网络加载本地数据
        Logger.d("isConnectedNetwork")
        return MediatorResult.Success(endOfPaginationReached = true)
      }

      // 第二步： 网络分页数据
      val page = pageKey ?: 0
      val response = JAViewer.SERVICE.getHomePage(page + 1)
      val wrappers = AVMOProvider.parseMovies(response.string(), page + 1)
      val endOfPaginationReached = wrappers.isEmpty()

      // 第三步： 插入数据库
      JAViewer.DB.withTransaction {
        movieDao.insertList(wrappers)
      }

      return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
    } catch (e: IOException) {
      return MediatorResult.Error(e)
    } catch (e: HttpException) {
      return MediatorResult.Error(e)
    }
  }

}
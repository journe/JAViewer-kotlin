package io.github.javiewer.model.repository

import androidx.paging.PagingConfig

object MovieListFactory {

  fun repository(): Repository =
    MovieListRepositoryImpl(
        pagingConfig
    )

  private val pagingConfig = PagingConfig(
      // 每页显示的数据的大小
      pageSize = 30,
      // 开启占位符
      enablePlaceholders = true,
      // 预刷新的距离，距离最后一个 item 多远时加载数据
      // 默认为 pageSize
      prefetchDistance = 4,
      /**
       * 初始化加载数量，默认为 pageSize * 3
       *
       * internal const val DEFAULT_INITIAL_PAGE_MULTIPLIER = 3
       * val initialLoadSize: Int = pageSize * DEFAULT_INITIAL_PAGE_MULTIPLIER
       */
      initialLoadSize = 30
  )
}
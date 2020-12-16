package io.github.javiewer.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.javiewer.JAViewer
import io.github.javiewer.model.database.AppDatabase
import io.github.javiewer.model.entity.Movie
import io.github.javiewer.model.network.BasicService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieListRepositoryImpl(
  private val pageConfig: PagingConfig
) : Repository {

  override fun fetchMovieList(): Flow<PagingData<Movie>> {
    return Pager(
        config = pageConfig,
        remoteMediator = JavHomeRemoteMediator()
    ) {
      JAViewer.DB.movieDao().getAll()
    }.flow
  }

  override suspend fun fetchPokemonInfo(name: String): Flow<JavResult<Movie>> {
    return flow {
      try {
        val pokemonDao = JAViewer.DB.movieDao()
        // 查询数据库是否存在，如果不存在请求网络
        val infoModel = pokemonDao.getByCode(name)
        if (infoModel == null) {
          // 网络请求
//          val netWorkPokemonInfo = api.fetchPokemonInfo(name)

          // 将网路请求的数据，换转成的数据库的 model，之后插入数据库
//          infoModel = PokemonInfoEntity.convert2PokemonInfoEntity(netWorkPokemonInfo)
          // 插入更新数据库
//          pokemonDao.insertPokemon(infoModel)
        }
        // 将数据源的 model 转换成上层用到的 model，
        // ui 不能直接持有数据源，防止数据源的变化，影响上层的 ui
//        val model = mapper2InfoModel.map(infoModel)

        // 发射转换后的数据
        emit(JavResult.Success(infoModel))
      } catch (e: Exception) {
        emit(JavResult.Failure(e.cause))
      }
    }.flowOn(Dispatchers.IO) // 通过 flowOn 切换到 io 线程
  }

//  override suspend fun fetchPokemonByParameter(parameter: String): Flow<PagingData<Movie>> {
//    return Pager(pageConfig) {
//      // 加载数据库的数据
//      db.pokemonDao()
//          .pokemonInfoByParameter(parameter)
//    }.flow.map { pagingData ->
//      // 数据映射，数据库实体 PersonEntity ——>  上层用到的实体 Person
//      pagingData.map { mapper2ItemMolde.map(it) }
//    }
//  }
}
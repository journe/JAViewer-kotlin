package io.github.javiewer.model.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import io.github.javiewer.model.entity.Movie

/**
 * Created by journey on 2020/5/19.
 */
@Dao
interface MovieDao : BaseDao<Movie> {

  @Query("SELECT * FROM Movie")
  fun getAll(): PagingSource<Int, Movie>

  @Query("SELECT * FROM Movie WHERE code LIKE :code")
  suspend fun getByCode(code: String): Movie

}
package io.github.javiewer.database.dao

import androidx.room.Dao
import io.github.javiewer.adapter.item.Movie

/**
 * Created by journey on 2020/5/19.
 */
@Dao
interface MovieDao : BaseDao<Movie> {
}
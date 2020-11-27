package io.github.javiewer.model.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(vararg obj: T)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertList(list: List<T>)

  @Update
  fun update(vararg obj: T)

  @Delete
  suspend fun delete(vararg obj: T)

}
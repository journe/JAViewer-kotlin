package io.github.javiewer.database.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.javiewer.database.TopicModelMinimal

/**
 *Created by chrissen on 2020/6/28
 */
@Dao
interface SearchHistoryDao : BaseDao<TopicModelMinimal> {

  @Query("SELECT * FROM TopicModelMinimal ORDER BY postCount DESC LIMIT 20 ")
  suspend fun getList(): List<TopicModelMinimal>?

  @Query("DELETE FROM TopicModelMinimal")
  suspend fun deleteAll()

}
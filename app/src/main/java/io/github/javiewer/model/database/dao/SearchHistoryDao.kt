package io.github.javiewer.model.database.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.javiewer.model.database.TopicModelMinimal

//@Dao
//interface SearchHistoryDao : BaseDao<TopicModelMinimal> {
//
//  @Query("SELECT * FROM TopicModelMinimal ORDER BY postCount DESC LIMIT 20 ")
//  suspend fun getList(): List<TopicModelMinimal>?
//
//  @Query("DELETE FROM TopicModelMinimal")
//  suspend fun deleteAll()
//
//}
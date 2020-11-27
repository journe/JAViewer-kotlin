package io.github.javiewer.model.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import io.github.javiewer.model.database.TopicModelMinimal

/**
 * Created by journey on 2020/5/19.
 */
@Dao
interface UserInfoDao : BaseDao<TopicModelMinimal> {
  @Query("SELECT * FROM TopicModelMinimal")
  fun getAll(): List<TopicModelMinimal>

  @Query("SELECT * FROM TopicModelMinimal WHERE id = :id LIMIT 1")
  suspend fun getUserById(id: String): TopicModelMinimal?

  @Query("SELECT * FROM TopicModelMinimal WHERE id = :id LIMIT 1")
  fun getUserByIdLive(id: String): LiveData<TopicModelMinimal>?

  @Query("SELECT COUNT(*) FROM TopicModelMinimal")
  suspend fun getCount(): Long

  @Query("SELECT * FROM TopicModelMinimal ORDER BY postCount ASC")
  fun dataSource(): DataSource.Factory<Int, TopicModelMinimal>

  @Query("SELECT MAX(postCount) + 1 FROM TopicModelMinimal")
  fun getNextIndex(): Int

  @Query("DELETE FROM TopicModelMinimal")
  fun deleteAll()
}
package io.github.javiewer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.javiewer.JAViewer
import io.github.javiewer.JAViewer.Companion
import io.github.javiewer.database.dao.SearchHistoryDao
import io.github.javiewer.database.dao.UserInfoDao
import kotlin.reflect.KProperty

/**
 * Created by journey on 2020/5/18.
 */
@Database(
    entities = [TopicModelMinimal::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun userInfoDao(): UserInfoDao
  abstract fun searchHistoryDao(): SearchHistoryDao

  operator fun getValue(
    companion: JAViewer.Companion,
    property: KProperty<*>
  ): AppDatabase {
    return getInstance()
  }

  companion object {
    private const val DATABASE_NAME: String = "JAV.db"

    // For Singleton instantiation
    @Volatile
    private var instance: AppDatabase? = null
    fun getInstance(context: Context = JAViewer.application): AppDatabase {
      return instance ?: synchronized(this) {
        instance ?: buildDatabase(context).also { instance = it }
      }
    }

    // Create and pre-populate the database. See this article for more details:
    // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
    private fun buildDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(
          context, AppDatabase::class.java,
          DATABASE_NAME
      )
          .fallbackToDestructiveMigration()
          .build()
    }
  }
}

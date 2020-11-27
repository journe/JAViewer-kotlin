package io.github.javiewer.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by journey on 2020/5/18.
 */
@Entity
data class TopicModelMinimal(
    @PrimaryKey
    var id: String = "",
    var title: String? = null,
    var type: String? = null,
    var content: String? = null,
    var cover: String? = null,
    var postCount: Int? = null
)
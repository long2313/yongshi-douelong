package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 地图实体类
 */
@Entity(
    tableName = "map",
    indices = [
        Index("name", unique = true)
    ]
)
data class Map(
    @PrimaryKey(autoGenerate = true)
    val map_id: Long = 0,
    
    val name: String,
    var description: String? = null,
    var level_required: Int = 1,
    var is_active: Boolean = true
)
package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 成就实体类
 */
@Entity(
    tableName = "achievement",
    indices = [
        Index("name", unique = true),
        Index("category")
    ]
)
data class Achievement(
    @PrimaryKey(autoGenerate = true)
    val achievement_id: Long = 0,
    
    val name: String,
    val description: String,
    val category: String? = null,
    val icon_path: String? = null,
    val reward_type: String? = null,
    val reward_value: Int = 0
)
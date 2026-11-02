package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 怪物实体类
 * 对应数据库中的Monster表
 */
@Entity(
    tableName = "monster",
    indices = [
        Index("name"),
        Index("difficulty")
    ]
)
data class Monster(
    @PrimaryKey(autoGenerate = true)
    val monster_id: Long = 0,
    
    val name: String,
    val description: String? = null,
    val level: Int = 1,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val exp_reward: Int = 0,
    val gold_reward: Int = 0,
    val difficulty: String,
    val is_boss: Boolean = false,
    val image_path: String? = null
)
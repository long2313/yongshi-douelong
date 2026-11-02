package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 装备实体类
 * 对应数据库中的Equipment表
 */
@Entity(
    tableName = "equipment",
    indices = [
        Index("name"),
        Index("type"),
        Index("rarity")
    ]
)
data class Equipment(
    @PrimaryKey(autoGenerate = true)
    val equipment_id: Long = 0,
    
    val name: String,
    val description: String? = null,
    val type: String,
    val attack_bonus: Int = 0,
    val defense_bonus: Int = 0,
    val hp_bonus: Int = 0,
    val level_required: Int = 1,
    val rarity: String,
    val image_path: String? = null
)
package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 物品实体类
 * 对应数据库中的Item表
 */
@Entity(
    tableName = "item",
    indices = [
        Index("name"),
        Index("type")
    ]
)
data class Item(
    @PrimaryKey(autoGenerate = true)
    val item_id: Long = 0,
    
    val name: String,
    val description: String? = null,
    val type: String,
    val effect_type: String? = null,
    val effect_value: Int = 0,
    val duration: Int = 0,
    val stack_limit: Int = 1,
    val sell_price: Int = 0,
    val image_path: String? = null
)
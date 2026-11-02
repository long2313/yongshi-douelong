package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 怪物掉落实体类
 * 对应数据库中的MonsterDrop表
 */
@Entity(
    tableName = "monster_drop",
    foreignKeys = [
        ForeignKey(
            entity = Monster::class,
            parentColumns = ["monster_id"],
            childColumns = ["monster_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Item::class,
            parentColumns = ["item_id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Equipment::class,
            parentColumns = ["equipment_id"],
            childColumns = ["equipment_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("monster_id"),
        Index("item_id"),
        Index("equipment_id")
    ]
)
data class MonsterDrop(
    @PrimaryKey(autoGenerate = true)
    val drop_id: Long = 0,
    
    val monster_id: Long,
    val item_id: Long? = null,
    val equipment_id: Long? = null,
    val drop_rate: Float,
    val min_quantity: Int = 1,
    val max_quantity: Int = 1
)
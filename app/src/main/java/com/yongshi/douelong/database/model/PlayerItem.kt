package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 玩家物品关联实体类
 * 对应数据库中的PlayerItem表
 */
@Entity(
    tableName = "player_item",
    foreignKeys = [
        ForeignKey(
            entity = Player::class,
            parentColumns = ["player_id"],
            childColumns = ["player_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Item::class,
            parentColumns = ["item_id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("player_id"),
        Index("item_id"),
        Index(value = ["player_id", "item_id"], unique = true)
    ]
)
data class PlayerItem(
    @PrimaryKey(autoGenerate = true)
    val player_item_id: Long = 0,
    
    val player_id: Long,
    val item_id: Long,
    var quantity: Int = 1,
    val acquired_at: Date = Date()
)
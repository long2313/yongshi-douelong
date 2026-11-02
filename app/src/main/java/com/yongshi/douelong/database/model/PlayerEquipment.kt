package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 玩家装备关联实体类
 * 对应数据库中的PlayerEquipment表
 */
@Entity(
    tableName = "player_equipment",
    foreignKeys = [
        ForeignKey(
            entity = Player::class,
            parentColumns = ["player_id"],
            childColumns = ["player_id"],
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
        Index("player_id"),
        Index("equipment_id"),
        Index(value = ["player_id", "equipment_id"], unique = true)
    ]
)
data class PlayerEquipment(
    @PrimaryKey(autoGenerate = true)
    val player_equipment_id: Long = 0,
    
    val player_id: Long,
    val equipment_id: Long,
    var is_equipped: Boolean = false,
    val acquired_at: Date = Date()
)
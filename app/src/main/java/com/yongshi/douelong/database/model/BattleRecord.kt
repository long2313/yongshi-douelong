package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 战斗记录实体类
 * 对应数据库中的BattleRecord表
 */
@Entity(
    tableName = "battle_record",
    foreignKeys = [
        ForeignKey(
            entity = Player::class,
            parentColumns = ["player_id"],
            childColumns = ["player_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Monster::class,
            parentColumns = ["monster_id"],
            childColumns = ["monster_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MapNode::class,
            parentColumns = ["node_id"],
            childColumns = ["node_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("player_id"),
        Index("monster_id"),
        Index("node_id")
    ]
)
data class BattleRecord(
    @PrimaryKey(autoGenerate = true)
    val battle_id: Long = 0,
    
    val player_id: Long,
    val monster_id: Long,
    val node_id: Long? = null,
    val map_id: Long? = null,
    val start_time: Date = Date(),
    var end_time: Date? = null,
    var result: String? = null,
    var exp_gained: Int = 0,
    var gold_gained: Int = 0
)
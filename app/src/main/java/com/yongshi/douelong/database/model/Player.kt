package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 玩家实体类
 */
@Entity(
    tableName = "player",
    foreignKeys = [
        ForeignKey(
            entity = MapNode::class,
            parentColumns = ["node_id"],
            childColumns = ["current_location"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("name"),
        Index("current_location")
    ]
)
data class Player(
    @PrimaryKey(autoGenerate = true)
    val player_id: Long = 0,
    
    val name: String,
    var level: Int = 1,
    var exp: Int = 0,
    var max_hp: Int = 100,
    var current_hp: Int = 100,
    var attack: Int = 10,
    var defense: Int = 5,
    var gold: Int = 0,
    var current_location: Long? = null,
    
    // 时间戳
    val created_at: Date = Date(),
    var updated_at: Date = Date()
)
package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 游戏存档实体类
 */
@Entity(
    tableName = "game_save",
    foreignKeys = [
        ForeignKey(
            entity = Player::class,
            parentColumns = ["player_id"],
            childColumns = ["player_id"]
        ),
        ForeignKey(
            entity = MapNode::class,
            parentColumns = ["node_id"],
            childColumns = ["current_node_id"]
        ),
        ForeignKey(
            entity = Map::class,
            parentColumns = ["map_id"],
            childColumns = ["current_map_id"]
        )
    ],
    indices = [
        Index("player_id"),
        Index("save_time"),
        Index("current_node_id"),  
        Index("current_map_id")    
    ]
)
data class GameSave(
    @PrimaryKey(autoGenerate = true)
    val save_id: Long = 0,
    
    val player_id: Long,
    var save_name: String,
    val save_time: Date,
    var current_node_id: Long = 1,
    var current_map_id: Long = 1,
    var player_state: String? = null,
    var is_auto_save: Boolean = false
)
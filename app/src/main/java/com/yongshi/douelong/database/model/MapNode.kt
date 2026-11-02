package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 地图节点实体类
 */
@Entity(
    tableName = "map_node",
    foreignKeys = [
        ForeignKey(
            entity = Map::class,
            parentColumns = ["map_id"],
            childColumns = ["map_id"]
        )
    ],
    indices = [
        Index("map_id"),
        Index("node_type")
    ]
)
data class MapNode(
    @PrimaryKey(autoGenerate = true)
    val node_id: Long = 0,
    
    val map_id: Long,
    val name: String,
    var description: String? = null,
    val node_type: String, // 节点类型：NORMAL, BATTLE, SHOP, REST, BOSS 等
    val x_coordinate: Int? = null,
    val y_coordinate: Int? = null,
    var is_starting_point: Boolean = false,
    var is_dragon_lair: Boolean = false
)
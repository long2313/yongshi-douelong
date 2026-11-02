package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 节点连接实体类
 * 描述两个地图节点之间的连接关系
 */
@Entity(
    tableName = "node_connection",
    foreignKeys = [
        ForeignKey(
            entity = MapNode::class,
            parentColumns = ["node_id"],
            childColumns = ["from_node_id"]
        ),
        ForeignKey(
            entity = MapNode::class,
            parentColumns = ["node_id"],
            childColumns = ["to_node_id"]
        )
    ],
    indices = [
        Index(value = ["from_node_id", "to_node_id"], unique = true),
        Index("from_node_id"),
        Index("to_node_id")
    ]
)
data class NodeConnection(
    @PrimaryKey(autoGenerate = true)
    val connection_id: Long = 0,
    
    val from_node_id: Long,
    val to_node_id: Long,
    var direction: String? = null, // NORTH, SOUTH, EAST, WEST 等
    var is_locked: Boolean = false,
    var unlock_condition: String? = null
)
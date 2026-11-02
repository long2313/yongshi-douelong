package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 节点事件实体类
 * 对应数据库中的NodeEvent表
 */
@Entity(
    tableName = "node_event",
    foreignKeys = [
        ForeignKey(
            entity = MapNode::class,
            parentColumns = ["node_id"],
            childColumns = ["node_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("node_id"),
        Index("event_type")
    ]
)
data class NodeEvent(
    @PrimaryKey(autoGenerate = true)
    val event_id: Long = 0,
    
    val node_id: Long,
    val event_type: String,
    val event_data: String? = null,
    val trigger_condition: String? = null,
    val trigger_probability: Float = 1.0f,
    val is_one_time: Boolean = false
)
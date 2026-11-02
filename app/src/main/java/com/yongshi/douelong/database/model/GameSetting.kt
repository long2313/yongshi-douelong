package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 游戏设置实体类
 * 对应数据库中的GameSetting表
 */
@Entity(
    tableName = "game_setting",
    foreignKeys = [
        ForeignKey(
            entity = Player::class,
            parentColumns = ["player_id"],
            childColumns = ["player_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("player_id", unique = true)
    ]
)
data class GameSetting(
    @PrimaryKey(autoGenerate = true)
    val setting_id: Long = 0,
    
    val player_id: Long,
    val music_volume: Float = 0.7f,
    val sound_effect_volume: Float = 0.7f,
    val text_speed: Int = 2,
    val difficulty: String = "normal",
    val language: String = "zh_CN",
    val vibration_enabled: Boolean = true,
    val notification_enabled: Boolean = true
)
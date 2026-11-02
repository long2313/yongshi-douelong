package com.yongshi.douelong.database.model

import androidx.room.*
import java.util.Date

/**
 * 玩家成就实体类
 * 对应数据库中的player_achievement表
 */
@Entity(
    tableName = "player_achievement",
    foreignKeys = [
        ForeignKey(
            entity = Player::class,
            parentColumns = ["player_id"],
            childColumns = ["player_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Achievement::class,
            parentColumns = ["achievement_id"],
            childColumns = ["achievement_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["player_id"]),
        Index(value = ["achievement_id"]),
        Index(value = ["player_id", "achievement_id"], unique = true)
    ]
)
data class PlayerAchievement(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "player_achievement_id")
    val playerAchievementId: Long? = null,
    
    @ColumnInfo(name = "player_id")
    val playerId: Long,
    
    @ColumnInfo(name = "achievement_id")
    val achievementId: Long,
    
    @ColumnInfo(name = "unlock_date")
    val unlockDate: Date = Date(),
    
    @ColumnInfo(name = "progress")
    var progress: Int = 0,
    
    @ColumnInfo(name = "is_claimed")
    var isClaimed: Boolean = false
)
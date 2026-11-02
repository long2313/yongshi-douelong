package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 战斗回合实体类
 * 对应数据库中的BattleRound表
 */
@Entity(
    tableName = "battle_round",
    foreignKeys = [
        ForeignKey(
            entity = BattleRecord::class,
            parentColumns = ["battle_id"],
            childColumns = ["battle_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EnglishQuestion::class,
            parentColumns = ["question_id"],
            childColumns = ["question_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("battle_id"),
        Index("question_id")
    ]
)
data class BattleRound(
    @PrimaryKey(autoGenerate = true)
    val round_id: Long = 0,
    
    val battle_id: Long,
    val round_number: Int,
    val question_id: Long? = null,
    var player_answer: String? = null,
    var is_correct: Boolean? = null,
    var damage_dealt: Int = 0,
    var damage_received: Int = 0,
    var player_hp_after: Int? = null,
    var monster_hp_after: Int? = null
)
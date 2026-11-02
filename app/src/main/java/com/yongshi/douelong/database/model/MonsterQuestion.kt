package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 怪物题目关联实体类
 * 对应数据库中的MonsterQuestion表
 */
@Entity(
    tableName = "monster_question",
    foreignKeys = [
        ForeignKey(
            entity = Monster::class,
            parentColumns = ["monster_id"],
            childColumns = ["monster_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SpanishQuestion::class,
            parentColumns = ["question_id"],
            childColumns = ["question_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("monster_id"),
        Index("question_id"),
        Index(value = ["monster_id", "question_id"], unique = true)
    ]
)
data class MonsterQuestion(
    @PrimaryKey(autoGenerate = true)
    val monster_question_id: Long = 0,
    
    val monster_id: Long,
    val question_id: Long,
    val weight: Int = 1
)
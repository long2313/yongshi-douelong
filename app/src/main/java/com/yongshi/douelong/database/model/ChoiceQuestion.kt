package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 选择题实体类
 * 对应数据库中的ChoiceQuestion表
 */
@Entity(
    tableName = "choice_question",
    foreignKeys = [
        ForeignKey(
            entity = EnglishQuestion::class,
            parentColumns = ["question_id"],
            childColumns = ["question_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("question_id", unique = true)
    ]
)
data class ChoiceQuestion(
    @PrimaryKey(autoGenerate = true)
    val choice_question_id: Long = 0,
    
    val question_id: Long,
    val option_a: String,
    val option_b: String,
    val option_c: String,
    val option_d: String,
    val correct_option: String
)
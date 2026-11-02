package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 填空题实体类
 * 对应数据库中的FillBlankQuestion表
 */
@Entity(
    tableName = "fill_blank_question",
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
data class FillBlankQuestion(
    @PrimaryKey(autoGenerate = true)
    val fill_question_id: Long = 0,
    
    val question_id: Long,
    val complete_sentence: String,
    val sentence_with_blank: String,
    val correct_answer: String,
    val hint: String? = null
)
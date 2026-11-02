package com.yongshi.douelong.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 英语题目实体类
 * 对应数据库中的EnglishQuestion表
 */
@Entity(
    tableName = "english_question",
    indices = [
        Index("type"),
        Index("difficulty"),
        Index("category")
    ]
)
data class EnglishQuestion(
    @PrimaryKey(autoGenerate = true)
    val question_id: Long = 0,
    
    val content: String,
    val type: String,
    val difficulty: Int,
    val category: String? = null
)
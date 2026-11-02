package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.SpanishQuestion

/**
 * 西语题目数据访问接口
 */
@Dao
interface SpanishQuestionDao {
    /**
     * 插入新的西语题目
     * @param spanishQuestion 西语题目对象
     * @return 插入的题目ID
     */
    @Insert
    fun insert(spanishQuestion: SpanishQuestion): Long
    
    /**
     * 插入多个西语题目
     * @param spanishQuestions 西语题目列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(spanishQuestions: List<SpanishQuestion>): List<Long>
    
    /**
     * 更新西语题目
     * @param spanishQuestion 西语题目对象
     * @return 更新的行数
     */
    @Update
    fun update(spanishQuestion: SpanishQuestion): Int
    
    /**
     * 删除西语题目
     * @param spanishQuestion 西语题目对象
     * @return 删除的行数
     */
    @Delete
    fun delete(spanishQuestion: SpanishQuestion): Int
    
    /**
     * 根据ID获取西语题目
     * @param id 题目ID
     * @return 西语题目对象
     */
    @Query("SELECT * FROM spanish_question WHERE question_id = :id")
    fun findById(id: Long): SpanishQuestion?
    
    /**
     * 获取所有西语题目
     * @return 西语题目列表
     */
    @Query("SELECT * FROM spanish_question")
    fun findAll(): List<SpanishQuestion>
    
    /**
     * 获取所有西语题目（LiveData形式，可观察）
     * @return 西语题目LiveData列表
     */
    @Query("SELECT * FROM spanish_question")
    fun observeAll(): LiveData<List<SpanishQuestion>>
    
    /**
     * 根据类型获取西语题目
     * @param type 题目类型（填空题/选择题）
     * @return 西语题目列表
     */
    @Query("SELECT * FROM spanish_question WHERE type = :type")
    fun findByType(type: String): List<SpanishQuestion>
    
    /**
     * 根据难度获取西语题目
     * @param difficulty 难度级别
     * @return 西语题目列表
     */
    @Query("SELECT * FROM spanish_question WHERE difficulty = :difficulty")
    fun findByDifficulty(difficulty: Int): List<SpanishQuestion>
    
    /**
     * 获取指定难度范围的西语题目
     * @param minDifficulty 最小难度
     * @param maxDifficulty 最大难度
     * @return 西语题目列表
     */
    @Query("SELECT * FROM spanish_question WHERE difficulty BETWEEN :minDifficulty AND :maxDifficulty")
    fun findByDifficultyRange(minDifficulty: Int, maxDifficulty: Int): List<SpanishQuestion>
    
    /**
     * 根据分类获取西语题目
     * @param category 题目分类
     * @return 西语题目列表
     */
    @Query("SELECT * FROM spanish_question WHERE category = :category")
    fun findByCategory(category: String): List<SpanishQuestion>
    
    /**
     * 获取随机西语题目
     * @param limit 题目数量
     * @return 西语题目列表
     */
    @Query("SELECT * FROM spanish_question ORDER BY RANDOM() LIMIT :limit")
    fun findRandom(limit: Int): List<SpanishQuestion>
    
    /**
     * 获取指定难度的随机西语题目
     * @param difficulty 难度级别
     * @param limit 题目数量
     * @return 西语题目列表
     */
    @Query("SELECT * FROM spanish_question WHERE difficulty = :difficulty ORDER BY RANDOM() LIMIT :limit")
    fun findRandomByDifficulty(difficulty: Int, limit: Int): List<SpanishQuestion>
    
    /**
     * 根据难度随机获取一个问题
     * @param difficulty 难度级别（easy/medium/hard）
     * @return 随机的英语问题
     */
    @Query("SELECT * FROM spanish_question WHERE difficulty = :difficulty ORDER BY RANDOM() LIMIT 1")
    fun getRandomQuestionByDifficulty(difficulty: String): SpanishQuestion?

    @Query("""
        SELECT * FROM spanish_question 
        WHERE difficulty BETWEEN :minDifficulty AND :maxDifficulty 
        ORDER BY RANDOM() 
        LIMIT :limit
    """)
    fun getRandomQuestionsByDifficultyRange(
        minDifficulty: Int,
        maxDifficulty: Int,
        limit: Int
    ): List<SpanishQuestion>

    @Query("SELECT * FROM spanish_question WHERE difficulty = :difficulty")
    fun getQuestionsByDifficulty(difficulty: Int): List<SpanishQuestion>

    @Query("SELECT * FROM spanish_question WHERE type = :type")
    fun getQuestionsByType(type: String): List<SpanishQuestion>

    /**
     * 获取指定难度范围内的随机问题，排除已使用的问题ID
     * @param minDifficulty 最小难度
     * @param maxDifficulty 最大难度
     * @param limit 返回的问题数量
     * @param excludeIds 要排除的问题ID列表
     * @return 随机英语问题列表
     */
    @Query("""
        SELECT * FROM spanish_question 
        WHERE difficulty BETWEEN :minDifficulty AND :maxDifficulty 
        AND question_id NOT IN (:excludeIds)
        ORDER BY RANDOM() 
        LIMIT :limit
    """)
    fun getRandomQuestionsByDifficultyRangeExcluding(
        minDifficulty: Int,
        maxDifficulty: Int,
        limit: Int,
        excludeIds: List<Long>
    ): List<SpanishQuestion>
} 
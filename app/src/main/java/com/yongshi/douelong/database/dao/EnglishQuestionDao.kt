package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.EnglishQuestion

/**
 * 英语题目数据访问接口
 */
@Dao
interface EnglishQuestionDao {
    /**
     * 插入新的英语题目
     * @param englishQuestion 英语题目对象
     * @return 插入的题目ID
     */
    @Insert
    fun insert(englishQuestion: EnglishQuestion): Long
    
    /**
     * 插入多个英语题目
     * @param englishQuestions 英语题目列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(englishQuestions: List<EnglishQuestion>): List<Long>
    
    /**
     * 更新英语题目
     * @param englishQuestion 英语题目对象
     * @return 更新的行数
     */
    @Update
    fun update(englishQuestion: EnglishQuestion): Int
    
    /**
     * 删除英语题目
     * @param englishQuestion 英语题目对象
     * @return 删除的行数
     */
    @Delete
    fun delete(englishQuestion: EnglishQuestion): Int
    
    /**
     * 根据ID获取英语题目
     * @param id 题目ID
     * @return 英语题目对象
     */
    @Query("SELECT * FROM english_question WHERE question_id = :id")
    fun findById(id: Long): EnglishQuestion?
    
    /**
     * 获取所有英语题目
     * @return 英语题目列表
     */
    @Query("SELECT * FROM english_question")
    fun findAll(): List<EnglishQuestion>
    
    /**
     * 获取所有英语题目（LiveData形式，可观察）
     * @return 英语题目LiveData列表
     */
    @Query("SELECT * FROM english_question")
    fun observeAll(): LiveData<List<EnglishQuestion>>
    
    /**
     * 根据类型获取英语题目
     * @param type 题目类型（填空题/选择题）
     * @return 英语题目列表
     */
    @Query("SELECT * FROM english_question WHERE type = :type")
    fun findByType(type: String): List<EnglishQuestion>
    
    /**
     * 根据难度获取英语题目
     * @param difficulty 难度级别
     * @return 英语题目列表
     */
    @Query("SELECT * FROM english_question WHERE difficulty = :difficulty")
    fun findByDifficulty(difficulty: Int): List<EnglishQuestion>
    
    /**
     * 获取指定难度范围的英语题目
     * @param minDifficulty 最小难度
     * @param maxDifficulty 最大难度
     * @return 英语题目列表
     */
    @Query("SELECT * FROM english_question WHERE difficulty BETWEEN :minDifficulty AND :maxDifficulty")
    fun findByDifficultyRange(minDifficulty: Int, maxDifficulty: Int): List<EnglishQuestion>
    
    /**
     * 根据分类获取英语题目
     * @param category 题目分类
     * @return 英语题目列表
     */
    @Query("SELECT * FROM english_question WHERE category = :category")
    fun findByCategory(category: String): List<EnglishQuestion>
    
    /**
     * 获取随机英语题目
     * @param limit 题目数量
     * @return 英语题目列表
     */
    @Query("SELECT * FROM english_question ORDER BY RANDOM() LIMIT :limit")
    fun findRandom(limit: Int): List<EnglishQuestion>
    
    /**
     * 获取指定难度的随机英语题目
     * @param difficulty 难度级别
     * @param limit 题目数量
     * @return 英语题目列表
     */
    @Query("SELECT * FROM english_question WHERE difficulty = :difficulty ORDER BY RANDOM() LIMIT :limit")
    fun findRandomByDifficulty(difficulty: Int, limit: Int): List<EnglishQuestion>
    
    /**
     * 根据难度随机获取一个问题
     * @param difficulty 难度级别（easy/medium/hard）
     * @return 随机的英语问题
     */
    @Query("SELECT * FROM english_question WHERE difficulty = :difficulty ORDER BY RANDOM() LIMIT 1")
    fun getRandomQuestionByDifficulty(difficulty: String): EnglishQuestion?

    @Query("""
        SELECT * FROM english_question 
        WHERE difficulty BETWEEN :minDifficulty AND :maxDifficulty 
        ORDER BY RANDOM() 
        LIMIT :limit
    """)
    fun getRandomQuestionsByDifficultyRange(
        minDifficulty: Int,
        maxDifficulty: Int,
        limit: Int
    ): List<EnglishQuestion>

    @Query("SELECT * FROM english_question WHERE difficulty = :difficulty")
    fun getQuestionsByDifficulty(difficulty: Int): List<EnglishQuestion>

    @Query("SELECT * FROM english_question WHERE type = :type")
    fun getQuestionsByType(type: String): List<EnglishQuestion>

    /**
     * 获取指定难度范围内的随机问题，排除已使用的问题ID
     * @param minDifficulty 最小难度
     * @param maxDifficulty 最大难度
     * @param limit 返回的问题数量
     * @param excludeIds 要排除的问题ID列表
     * @return 随机英语问题列表
     */
    @Query("""
        SELECT * FROM english_question 
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
    ): List<EnglishQuestion>
} 
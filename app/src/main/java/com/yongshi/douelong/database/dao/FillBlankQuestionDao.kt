package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.FillBlankQuestion

/**
 * 填空题数据访问接口
 */
@Dao
interface FillBlankQuestionDao {
    /**
     * 插入新的填空题
     * @param fillBlankQuestion 填空题对象
     * @return 插入的题目ID
     */
    @Insert
    fun insert(fillBlankQuestion: FillBlankQuestion): Long
    
    /**
     * 插入多个填空题
     * @param fillBlankQuestions 填空题列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(fillBlankQuestions: List<FillBlankQuestion>): List<Long>
    
    /**
     * 更新填空题
     * @param fillBlankQuestion 填空题对象
     * @return 更新的行数
     */
    @Update
    fun update(fillBlankQuestion: FillBlankQuestion): Int
    
    /**
     * 删除填空题
     * @param fillBlankQuestion 填空题对象
     * @return 删除的行数
     */
    @Delete
    fun delete(fillBlankQuestion: FillBlankQuestion): Int
    
    /**
     * 根据ID获取填空题
     * @param id 填空题ID
     * @return 填空题对象
     */
    @Query("SELECT * FROM fill_blank_question WHERE fill_question_id = :id")
    fun findById(id: Long): FillBlankQuestion?
    
    /**
     * 根据问题ID获取填空题
     * @param questionId 问题ID
     * @return 填空题对象
     */
    @Query("SELECT * FROM fill_blank_question WHERE question_id = :questionId")
    fun findByQuestionId(questionId: Long): FillBlankQuestion?
    
    /**
     * 获取所有填空题
     * @return 填空题列表
     */
    @Query("SELECT * FROM fill_blank_question")
    fun findAll(): List<FillBlankQuestion>
    
    /**
     * 获取所有填空题（LiveData形式，可观察）
     * @return 填空题LiveData列表
     */
    @Query("SELECT * FROM fill_blank_question")
    fun observeAll(): LiveData<List<FillBlankQuestion>>
    
    /**
     * 获取包含特定答案的填空题
     * @param answer 答案关键词
     * @return 填空题列表
     */
    @Query("SELECT * FROM fill_blank_question WHERE correct_answer LIKE '%' || :answer || '%'")
    fun findByAnswer(answer: String): List<FillBlankQuestion>
    
    /**
     * 获取包含特定句子的填空题
     * @param sentence 句子关键词
     * @return 填空题列表
     */
    @Query("SELECT * FROM fill_blank_question WHERE complete_sentence LIKE '%' || :sentence || '%'")
    fun findBySentence(sentence: String): List<FillBlankQuestion>
    
    /**
     * 通过西语题目信息获取填空题
     * 用于联合查询，获取特定难度的填空题
     * @param difficulty 难度级别
     * @return 填空题列表
     */
    @Query("""
        SELECT fbq.* FROM fill_blank_question fbq
        INNER JOIN spanish_question eq ON fbq.question_id = eq.question_id
        WHERE eq.difficulty = :difficulty
    """)
    fun findByDifficulty(difficulty: Int): List<FillBlankQuestion>
    
    /**
     * 获取随机填空题
     * @param limit 题目数量
     * @return 填空题列表
     */
    @Query("SELECT * FROM fill_blank_question ORDER BY RANDOM() LIMIT :limit")
    fun findRandom(limit: Int): List<FillBlankQuestion>
} 
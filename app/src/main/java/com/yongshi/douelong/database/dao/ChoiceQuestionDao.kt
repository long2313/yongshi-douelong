package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.ChoiceQuestion

/**
 * 选择题数据访问接口
 */
@Dao
interface ChoiceQuestionDao {
    /**
     * 插入新的选择题
     * @param choiceQuestion 选择题对象
     * @return 插入的题目ID
     */
    @Insert
    fun insert(choiceQuestion: ChoiceQuestion): Long
    
    /**
     * 插入多个选择题
     * @param choiceQuestions 选择题列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(choiceQuestions: List<ChoiceQuestion>): List<Long>
    
    /**
     * 更新选择题
     * @param choiceQuestion 选择题对象
     * @return 更新的行数
     */
    @Update
    fun update(choiceQuestion: ChoiceQuestion): Int
    
    /**
     * 删除选择题
     * @param choiceQuestion 选择题对象
     * @return 删除的行数
     */
    @Delete
    fun delete(choiceQuestion: ChoiceQuestion): Int
    
    /**
     * 根据ID获取选择题
     * @param id 选择题ID
     * @return 选择题对象
     */
    @Query("SELECT * FROM choice_question WHERE choice_question_id = :id")
    fun findById(id: Long): ChoiceQuestion?
    
    /**
     * 根据问题ID获取选择题
     * @param questionId 问题ID
     * @return 选择题对象
     */
    @Query("SELECT * FROM choice_question WHERE question_id = :questionId")
    fun findByQuestionId(questionId: Long): ChoiceQuestion?
    
    /**
     * 获取所有选择题
     * @return 选择题列表
     */
    @Query("SELECT * FROM choice_question")
    fun findAll(): List<ChoiceQuestion>
    
    /**
     * 获取所有选择题（LiveData形式，可观察）
     * @return 选择题LiveData列表
     */
    @Query("SELECT * FROM choice_question")
    fun observeAll(): LiveData<List<ChoiceQuestion>>
    
    /**
     * 根据正确选项获取选择题
     * @param correctOption 正确选项（A/B/C/D）
     * @return 选择题列表
     */
    @Query("SELECT * FROM choice_question WHERE correct_option = :correctOption")
    fun findByCorrectOption(correctOption: String): List<ChoiceQuestion>
    
    /**
     * 通过西语题目信息获取选择题
     * 用于联合查询，获取特定难度的选择题
     * @param difficulty 难度级别
     * @return 选择题列表
     */
    @Query("""
        SELECT cq.* FROM choice_question cq
        INNER JOIN spanish_question eq ON cq.question_id = eq.question_id
        WHERE eq.difficulty = :difficulty
    """)
    fun findByDifficulty(difficulty: Int): List<ChoiceQuestion>
    
    /**
     * 获取随机选择题
     * @param limit 题目数量
     * @return 选择题列表
     */
    @Query("SELECT * FROM choice_question ORDER BY RANDOM() LIMIT :limit")
    fun findRandom(limit: Int): List<ChoiceQuestion>
} 
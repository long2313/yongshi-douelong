package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.EnglishQuestion
import com.yongshi.douelong.database.model.MonsterQuestion

/**
 * 怪物题目关联数据访问接口
 */
@Dao
interface MonsterQuestionDao {
    /**
     * 插入新的怪物题目关联
     * @param monsterQuestion 怪物题目关联对象
     * @return 插入的关联ID
     */
    @Insert
    fun insert(monsterQuestion: MonsterQuestion): Long
    
    /**
     * 插入多个怪物题目关联
     * @param monsterQuestions 怪物题目关联列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(monsterQuestions: List<MonsterQuestion>): List<Long>
    
    /**
     * 更新怪物题目关联
     * @param monsterQuestion 怪物题目关联对象
     * @return 更新的行数
     */
    @Update
    fun update(monsterQuestion: MonsterQuestion): Int
    
    /**
     * 删除怪物题目关联
     * @param monsterQuestion 怪物题目关联对象
     * @return 删除的行数
     */
    @Delete
    fun delete(monsterQuestion: MonsterQuestion): Int
    
    /**
     * 根据ID获取怪物题目关联
     * @param id 关联ID
     * @return 怪物题目关联对象
     */
    @Query("SELECT * FROM monster_question WHERE monster_question_id = :id")
    fun findById(id: Long): MonsterQuestion?
    
    /**
     * 获取指定怪物的所有题目关联
     * @param monsterId 怪物ID
     * @return 怪物题目关联列表
     */
    @Query("SELECT * FROM monster_question WHERE monster_id = :monsterId")
    fun findByMonsterId(monsterId: Long): List<MonsterQuestion>
    
    /**
     * 获取指定怪物的所有题目关联（LiveData形式，可观察）
     * @param monsterId 怪物ID
     * @return 怪物题目关联LiveData列表
     */
    @Query("SELECT * FROM monster_question WHERE monster_id = :monsterId")
    fun observeByMonsterId(monsterId: Long): LiveData<List<MonsterQuestion>>
    
    /**
     * 获取指定题目的所有怪物关联
     * @param questionId 题目ID
     * @return 怪物题目关联列表
     */
    @Query("SELECT * FROM monster_question WHERE question_id = :questionId")
    fun findByQuestionId(questionId: Long): List<MonsterQuestion>
    
    /**
     * 检查怪物是否关联了指定题目
     * @param monsterId 怪物ID
     * @param questionId 题目ID
     * @return 怪物题目关联对象，如果不存在则为null
     */
    @Query("SELECT * FROM monster_question WHERE monster_id = :monsterId AND question_id = :questionId LIMIT 1")
    fun findByMonsterAndQuestion(monsterId: Long, questionId: Long): MonsterQuestion?
    
    /**
     * 获取怪物的所有英语题目
     * 联合查询，获取怪物关联的所有英语题目详情
     * @param monsterId 怪物ID
     * @return 英语题目列表
     */
    @Query("""
        SELECT eq.* FROM english_question eq
        INNER JOIN monster_question mq ON eq.question_id = mq.question_id
        WHERE mq.monster_id = :monsterId
    """)
    fun getEnglishQuestionsForMonster(monsterId: Long): List<EnglishQuestion>
    
    /**
     * 获取怪物的随机英语题目
     * 基于权重随机选择，权重高的题目有更高的概率被选中
     * @param monsterId 怪物ID
     * @param limit 题目数量
     * @return 英语题目列表
     */
    @Query("""
        SELECT eq.* FROM english_question eq
        INNER JOIN monster_question mq ON eq.question_id = mq.question_id
        WHERE mq.monster_id = :monsterId
        ORDER BY 
          CASE 
            WHEN mq.weight = 0 THEN RANDOM() 
            ELSE RANDOM() * mq.weight 
          END DESC
        LIMIT :limit
    """)
    fun getRandomQuestionsForMonster(monsterId: Long, limit: Int = 1): List<EnglishQuestion>
    
    /**
     * 更新题目权重
     * @param monsterQuestionId 怪物题目关联ID
     * @param weight 新权重
     * @return 更新的行数
     */
    @Query("UPDATE monster_question SET weight = :weight WHERE monster_question_id = :monsterQuestionId")
    fun updateWeight(monsterQuestionId: Long, weight: Int): Int
} 
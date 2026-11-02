package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.BattleRound

/**
 * 战斗回合数据访问接口
 */
@Dao
interface BattleRoundDao {
    /**
     * 插入新的战斗回合
     * @param battleRound 战斗回合对象
     * @return 插入的战斗回合ID
     */
    @Insert
    fun insert(battleRound: BattleRound): Long
    
    /**
     * 插入多个战斗回合
     * @param battleRounds 战斗回合列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(battleRounds: List<BattleRound>): List<Long>
    
    /**
     * 更新战斗回合
     * @param battleRound 战斗回合对象
     * @return 更新的行数
     */
    @Update
    fun update(battleRound: BattleRound): Int
    
    /**
     * 删除战斗回合
     * @param battleRound 战斗回合对象
     * @return 删除的行数
     */
    @Delete
    fun delete(battleRound: BattleRound): Int
    
    /**
     * 根据ID获取战斗回合
     * @param id 战斗回合ID
     * @return 战斗回合对象
     */
    @Query("SELECT * FROM battle_round WHERE round_id = :id")
    fun findById(id: Long): BattleRound?
    
    /**
     * 获取指定战斗的所有回合
     * @param battleId 战斗ID
     * @return 战斗回合列表
     */
    @Query("SELECT * FROM battle_round WHERE battle_id = :battleId ORDER BY round_number")
    fun findByBattleId(battleId: Long): List<BattleRound>
    
    /**
     * 获取指定战斗的所有回合（LiveData形式，可观察）
     * @param battleId 战斗ID
     * @return 战斗回合LiveData列表
     */
    @Query("SELECT * FROM battle_round WHERE battle_id = :battleId ORDER BY round_number ASC")
    fun observeByBattleId(battleId: Long): LiveData<List<BattleRound>>
    
    /**
     * 获取指定战斗的最后一个回合
     * @param battleId 战斗ID
     * @return 最后一个战斗回合
     */
    @Query("""
        SELECT * FROM battle_round 
        WHERE battle_id = :battleId 
        ORDER BY round_number DESC 
        LIMIT 1
    """)
    fun getLastRound(battleId: Long): BattleRound?
    
    /**
     * 获取使用特定题目的所有回合
     * @param questionId 题目ID
     * @return 使用该题目的战斗回合列表
     */
    @Query("SELECT * FROM battle_round WHERE question_id = :questionId")
    fun findByQuestionId(questionId: Long): List<BattleRound>
    
    /**
     * 获取玩家回答正确的回合
     * @param battleId 战斗ID
     * @return 回答正确的战斗回合列表
     */
    @Query("""
        SELECT * FROM battle_round 
        WHERE battle_id = :battleId AND is_correct = true
    """)
    fun getCorrectAnswerCount(battleId: Long): Int
    
    /**
     * 获取玩家回答错误的回合
     * @param battleId 战斗ID
     * @return 回答错误的战斗回合列表
     */
    @Query("""
        SELECT * FROM battle_round 
        WHERE battle_id = :battleId AND is_correct = false
    """)
    fun getWrongAnswerCount(battleId: Long): Int
    
    /**
     * 计算战斗中的总回合数
     * @param battleId 战斗ID
     * @return 总回合数
     */
    @Query("SELECT COUNT(*) FROM battle_round WHERE battle_id = :battleId")
    fun countRounds(battleId: Long): Int
} 
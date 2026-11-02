package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.BattleRecord
import java.util.Date

/**
 * 战斗记录数据访问接口
 */
@Dao
interface BattleRecordDao {
    /**
     * 插入新的战斗记录
     * @param battleRecord 战斗记录对象
     * @return 插入的战斗记录ID
     */
    @Insert
    fun insert(battleRecord: BattleRecord): Long
    
    /**
     * 插入多个战斗记录
     * @param battleRecords 战斗记录列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(battleRecords: List<BattleRecord>): List<Long>
    
    /**
     * 更新战斗记录
     * @param battleRecord 战斗记录对象
     * @return 更新的行数
     */
    @Update
    fun update(battleRecord: BattleRecord): Int
    
    /**
     * 删除战斗记录
     * @param battleRecord 战斗记录对象
     * @return 删除的行数
     */
    @Delete
    fun delete(battleRecord: BattleRecord): Int
    
    /**
     * 根据ID获取战斗记录
     * @param id 战斗记录ID
     * @return 战斗记录对象
     */
    @Query("SELECT * FROM battle_record WHERE battle_id = :id")
    fun findById(id: Long): BattleRecord?
    
    /**
     * 获取指定玩家的所有战斗记录
     * @param playerId 玩家ID
     * @return 战斗记录列表
     */
    @Query("SELECT * FROM battle_record WHERE player_id = :playerId")
    fun findByPlayerId(playerId: Long): List<BattleRecord>
    
    /**
     * 获取指定玩家的所有战斗记录（LiveData形式，可观察）
     * @param playerId 玩家ID
     * @return 战斗记录LiveData列表
     */
    @Query("SELECT * FROM battle_record WHERE player_id = :playerId ORDER BY start_time DESC")
    fun observeByPlayerId(playerId: Long): LiveData<List<BattleRecord>>
    
    /**
     * 获取指定怪物的所有战斗记录
     * @param monsterId 怪物ID
     * @return 战斗记录列表
     */
    @Query("SELECT * FROM battle_record WHERE monster_id = :monsterId")
    fun findByMonsterId(monsterId: Long): List<BattleRecord>
    
    /**
     * 获取指定地图节点的所有战斗记录
     * @param nodeId 地图节点ID
     * @return 战斗记录列表
     */
    @Query("SELECT * FROM battle_record WHERE node_id = :nodeId")
    fun findByNodeId(nodeId: Long): List<BattleRecord>
    
    /**
     * 获取指定时间范围内的战斗记录
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 战斗记录列表
     */
    @Query("SELECT * FROM battle_record WHERE start_time BETWEEN :startDate AND :endDate ORDER BY start_time DESC")
    fun findByTimeRange(startDate: Date, endDate: Date): List<BattleRecord>
    
    /**
     * 更新战斗结果
     * @param battleId 战斗记录ID
     * @param result 战斗结果
     * @param endTime 结束时间
     * @param expGained 获得的经验值
     * @param goldGained 获得的金币
     * @return 更新的行数
     */
    @Query("UPDATE battle_record SET result = :result, end_time = :endTime, exp_gained = :expGained, gold_gained = :goldGained WHERE battle_id = :battleId")
    fun updateBattleResult(battleId: Long, result: String?, endTime: Date, expGained: Int, goldGained: Int): Int
    
    /**
     * 获取最近的战斗记录
     * @param playerId 玩家ID
     * @param limit 记录数量限制
     * @return 战斗记录列表
     */
    @Query("""
        SELECT * FROM battle_record 
        WHERE player_id = :playerId 
        ORDER BY start_time DESC 
        LIMIT :limit
    """)
    fun getRecentBattles(playerId: Long, limit: Int): List<BattleRecord>

    

} 
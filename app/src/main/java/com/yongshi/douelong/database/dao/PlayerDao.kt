package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.Player

/**
 * 玩家数据访问接口
 */
@Dao
interface PlayerDao {
    /**
     * 插入新玩家
     * @param player 玩家对象
     * @return 插入的玩家ID
     */
    @Insert
    fun insert(player: Player): Long
    
    /**
     * 插入多个玩家
     * @param players 玩家列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(players: List<Player>): List<Long>
    
    /**
     * 更新玩家信息
     * @param player 玩家对象
     * @return 更新的行数
     */
    @Update
    fun update(player: Player): Int
    
    /**
     * 删除玩家
     * @param player 玩家对象
     * @return 删除的行数
     */
    @Delete
    fun delete(player: Player): Int
    
    /**
     * 根据ID删除玩家
     * @param id 玩家ID
     * @return 删除的行数
     */
    @Query("DELETE FROM player WHERE player_id = :id")
    fun deleteById(id: Long): Int
    
    /**
     * 根据ID获取玩家
     * @param id 玩家ID
     * @return 玩家对象
     */
    @Query("SELECT * FROM player WHERE player_id = :id")
    fun findById(id: Long): Player?
    
    /**
     * 根据名称查找玩家
     * @param name 玩家名称
     * @return 玩家对象
     */
    @Query("SELECT * FROM player WHERE name = :name LIMIT 1")
    fun findByName(name: String): Player?
    
    /**
     * 获取所有玩家
     * @return 玩家列表
     */
    @Query("SELECT * FROM player ORDER BY name ASC")
    fun findAll(): List<Player>
    
    /**
     * 获取所有玩家（LiveData形式，可观察）
     * @return 玩家LiveData列表
     */
    @Query("SELECT * FROM player ORDER BY name ASC")
    fun observeAll(): LiveData<List<Player>>
    
    /**
     * 获取指定等级以上的玩家
     * @param level 等级阈值
     * @return 符合条件的玩家列表
     */
    @Query("SELECT * FROM player WHERE level >= :level ORDER BY level DESC")
    fun findByLevelAbove(level: Int): List<Player>
    
    /**
     * 获取在指定位置的玩家
     * @param locationId 位置ID
     * @return 位于该位置的玩家列表
     */
    @Query("SELECT * FROM player WHERE current_location = :locationId")
    fun findByLocation(locationId: Long): List<Player>
    
    /**
     * 更新玩家的金币数量
     * @param playerId 玩家ID
     * @param amount 金币数量（可为负）
     * @return 更新的行数
     */
    @Query("UPDATE player SET gold = gold + :amount WHERE player_id = :playerId")
    fun updateGold(playerId: Long, amount: Int): Int
    
    /**
     * 更新玩家的经验值
     * @param playerId 玩家ID
     * @param amount 经验值
     * @return 更新的行数
     */
    @Query("UPDATE player SET exp = exp + :amount WHERE player_id = :playerId")
    fun updateExp(playerId: Long, amount: Int): Int
    
    /**
     * 更新玩家当前的血量
     * @param playerId 玩家ID
     * @param hp 血量值（可为负）
     * @return 更新的行数
     */
    @Query("UPDATE player SET current_hp = MIN(max_hp, current_hp + :hp) WHERE player_id = :playerId")
    fun updateHp(playerId: Long, hp: Int): Int
} 
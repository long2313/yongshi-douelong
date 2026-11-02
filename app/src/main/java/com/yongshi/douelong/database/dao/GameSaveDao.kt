package com.yongshi.douelong.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yongshi.douelong.database.model.GameSave
import java.util.Date

/**
 * 游戏存档数据访问接口
 */
@Dao
interface GameSaveDao {
    /**
     * 插入新的游戏存档
     * @param gameSave 游戏存档对象
     * @return 插入的记录ID
     */
    @Insert
    fun insert(gameSave: GameSave): Long
    
    /**
     * 更新游戏存档
     * @param gameSave 游戏存档对象
     * @return 更新的行数
     */
    @Update
    fun update(gameSave: GameSave): Int
    
    /**
     * 删除游戏存档
     * @param gameSave 要删除的游戏存档
     * @return 删除的行数
     */
    @Delete
    fun delete(gameSave: GameSave): Int
    
    /**
     * 根据ID删除游戏存档
     * @param saveId 存档ID
     * @return 删除的行数
     */
    @Query("DELETE FROM game_save WHERE save_id = :saveId")
    fun deleteById(saveId: Long): Int
    
    /**
     * 根据ID获取游戏存档
     * @param saveId 存档ID
     * @return 游戏存档对象
     */
    @Query("SELECT * FROM game_save WHERE save_id = :saveId")
    fun findById(saveId: Long): GameSave?
    
    /**
     * 获取玩家的所有存档
     * @param playerId 玩家ID
     * @return 存档列表
     */
    @Query("SELECT * FROM game_save WHERE player_id = :playerId ORDER BY save_time DESC")
    fun findByPlayerId(playerId: Long): List<GameSave>
    
    /**
     * 获取所有存档
     * @return 所有存档列表
     */
    @Query("SELECT * FROM game_save ORDER BY save_time DESC")
    fun findAll(): List<GameSave>
    
    /**
     * 获取玩家的最新存档
     * @param playerId 玩家ID
     * @return 最新的存档
     */
    @Query("SELECT * FROM game_save WHERE player_id = :playerId ORDER BY save_time DESC LIMIT 1")
    fun findLatestByPlayerId(playerId: Long): GameSave?
    
    /**
     * 获取玩家的自动存档
     * @param playerId 玩家ID
     * @return 自动存档列表
     */
    @Query("SELECT * FROM game_save WHERE player_id = :playerId AND is_auto_save = 1 ORDER BY save_time DESC")
    fun findAutoSavesByPlayerId(playerId: Long): List<GameSave>
    
    /**
     * 根据保存时间范围获取存档
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 符合时间范围的存档列表
     */
    @Query("SELECT * FROM game_save WHERE save_time BETWEEN :startTime AND :endTime ORDER BY save_time DESC")
    fun findByTimeRange(startTime: Date, endTime: Date): List<GameSave>
} 
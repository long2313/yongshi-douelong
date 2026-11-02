package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.PlayerEquipment

/**
 * 玩家装备数据访问接口
 */
@Dao
interface PlayerEquipmentDao {
    /**
     * 插入新的玩家装备关联
     * @param playerEquipment 玩家装备关联对象
     * @return 插入的关联ID
     */
    @Insert
    suspend fun insert(playerEquipment: PlayerEquipment): Long
    
    /**
     * 插入多个玩家装备关联
     * @param playerEquipments 玩家装备关联列表
     * @return 插入的ID列表
     */
    @Insert
    suspend fun insertAll(playerEquipments: List<PlayerEquipment>): List<Long>
    
    /**
     * 更新玩家装备关联
     * @param playerEquipment 玩家装备关联对象
     * @return 更新的行数
     */
    @Update
    suspend fun update(playerEquipment: PlayerEquipment): Int
    
    /**
     * 删除玩家装备关联
     * @param playerEquipment 玩家装备关联对象
     * @return 删除的行数
     */
    @Delete
    suspend fun delete(playerEquipment: PlayerEquipment): Int
    
    /**
     * 根据ID获取玩家装备关联
     * @param id 关联ID
     * @return 玩家装备关联对象
     */
    @Query("SELECT * FROM player_equipment WHERE player_equipment_id = :id")
    suspend fun findById(id: Long): PlayerEquipment?
    
    /**
     * 获取玩家所有装备
     * @param playerId 玩家ID
     * @return 玩家装备关联列表
     */
    @Query("SELECT * FROM player_equipment WHERE player_id = :playerId")
    suspend fun findByPlayerId(playerId: Long): List<PlayerEquipment>
    
    /**
     * 获取玩家所有装备（LiveData形式，可观察）
     * @param playerId 玩家ID
     * @return 玩家装备关联LiveData列表
     */
    @Query("SELECT * FROM player_equipment WHERE player_id = :playerId")
    fun observeByPlayerId(playerId: Long): LiveData<List<PlayerEquipment>>
    
    /**
     * 获取玩家已装备的装备
     * @param playerId 玩家ID
     * @return 玩家已装备的装备关联列表
     */
    @Query("SELECT * FROM player_equipment WHERE player_id = :playerId AND is_equipped = 1")
    suspend fun findEquippedByPlayerId(playerId: Long): List<PlayerEquipment>
    
    /**
     * 获取指定装备的所有玩家关联
     * @param equipmentId 装备ID
     * @return 拥有该装备的玩家关联列表
     */
    @Query("SELECT * FROM player_equipment WHERE equipment_id = :equipmentId")
    suspend fun findByEquipmentId(equipmentId: Long): List<PlayerEquipment>
    
    /**
     * 检查玩家是否拥有指定装备
     * @param playerId 玩家ID
     * @param equipmentId 装备ID
     * @return 玩家装备关联对象，如果不存在则为null
     */
    @Query("SELECT * FROM player_equipment WHERE player_id = :playerId AND equipment_id = :equipmentId LIMIT 1")
    suspend fun findByPlayerAndEquipment(playerId: Long, equipmentId: Long): PlayerEquipment?
    
    /**
     * 装备或卸下一件装备
     * @param playerEquipmentId 玩家装备关联ID
     * @param isEquipped 是否装备
     * @return 更新的行数
     */
    @Query("UPDATE player_equipment SET is_equipped = :isEquipped WHERE player_equipment_id = :playerEquipmentId")
    suspend fun updateEquipStatus(playerEquipmentId: Long, isEquipped: Boolean): Int
    
    /**
     * 卸下玩家所有同类型装备（用于装备新装备前）
     * @param playerId 玩家ID
     * @param equipmentType 装备类型
     * @return 更新的行数
     */
    @Query("""
        UPDATE player_equipment SET is_equipped = 0 
        WHERE player_id = :playerId AND is_equipped = 1 
        AND equipment_id IN (SELECT equipment_id FROM equipment WHERE type = :equipmentType)
    """)
    suspend fun unequipByType(playerId: Long, equipmentType: String): Int
    
    /**
     * 获取玩家所有装备及其详细信息
     * @param playerId 玩家ID
     * @return 装备与关联状态详情
     */
    @Transaction
    @Query("""
        SELECT e.*, pe.is_equipped, pe.player_equipment_id, pe.acquired_at 
        FROM equipment e
        INNER JOIN player_equipment pe ON e.equipment_id = pe.equipment_id
        WHERE pe.player_id = :playerId
    """)
    suspend fun getPlayerEquipmentDetails(playerId: Long): List<PlayerEquipmentWithDetails>
    
    /**
     * 获取玩家已装备的装备及其详细信息
     * @param playerId 玩家ID
     * @return 已装备的装备与关联状态详情
     */
    @Transaction
    @Query("""
        SELECT e.*, pe.is_equipped, pe.player_equipment_id, pe.acquired_at 
        FROM equipment e
        INNER JOIN player_equipment pe ON e.equipment_id = pe.equipment_id
        WHERE pe.player_id = :playerId AND pe.is_equipped = 1
    """)
    suspend fun getPlayerEquippedDetails(playerId: Long): List<PlayerEquipmentWithDetails>
}

/**
 * 玩家装备详情视图类
 * 用于组合Equipment和PlayerEquipment的信息
 */
data class PlayerEquipmentWithDetails(
    // 装备基本信息
    val equipment_id: Long,
    val name: String,
    val description: String?,
    val type: String,
    val attack_bonus: Int,
    val defense_bonus: Int,
    val hp_bonus: Int,
    val level_required: Int,
    val rarity: String,
    val image_path: String?,
    
    // 关联信息
    val is_equipped: Boolean,
    val player_equipment_id: Long,
    val acquired_at: java.util.Date
) 
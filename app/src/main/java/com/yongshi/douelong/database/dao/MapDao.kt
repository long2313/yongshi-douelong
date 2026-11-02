package com.yongshi.douelong.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yongshi.douelong.database.model.Map

/**
 * 地图数据访问接口
 */
@Dao
interface MapDao {
    /**
     * 插入新地图
     * @param map 地图对象
     * @return 插入的地图ID
     */
    @Insert
    fun insert(map: Map): Long
    
    /**
     * 批量插入地图
     * @param maps 地图列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(maps: List<Map>): List<Long>
    
    /**
     * 更新地图
     * @param map 地图对象
     * @return 更新的行数
     */
    @Update
    fun update(map: Map): Int
    
    /**
     * 删除地图
     * @param map 地图对象
     * @return 删除的行数
     */
    @Delete
    fun delete(map: Map): Int
    
    /**
     * 根据ID删除地图
     * @param mapId 地图ID
     * @return 删除的行数
     */
    @Query("DELETE FROM map WHERE map_id = :mapId")
    fun deleteById(mapId: Long): Int
    
    /**
     * 根据ID查找地图
     * @param mapId 地图ID
     * @return 地图对象
     */
    @Query("SELECT * FROM map WHERE map_id = :mapId")
    fun findById(mapId: Long): Map?
    
    /**
     * 根据名称查找地图
     * @param name 地图名称
     * @return 地图对象
     */
    @Query("SELECT * FROM map WHERE name = :name LIMIT 1")
    fun findByName(name: String): Map?
    
    /**
     * 获取所有地图
     * @return 所有地图列表
     */
    @Query("SELECT * FROM map")
    fun findAll(): List<Map>
    
    /**
     * 获取活跃的地图
     * @return 活跃地图列表
     */
    @Query("SELECT * FROM map WHERE is_active = 1")
    fun findActiveMap(): List<Map>
    
    /**
     * 获取特定等级要求的地图
     * @param level 等级要求
     * @return 符合等级要求的地图列表
     */
    @Query("SELECT * FROM map WHERE level_required <= :level AND is_active = 1")
    fun findMapsByLevelRequirement(level: Int): List<Map>
    
    /**
     * 更新地图活跃状态
     * @param mapId 地图ID
     * @param isActive 是否活跃
     * @return 更新的行数
     */
    @Query("UPDATE map SET is_active = :isActive WHERE map_id = :mapId")
    fun updateMapActiveStatus(mapId: Long, isActive: Boolean): Int

    /**
     * 统计地图总数（用于启动时判断是否需要初始化默认地图）
     * @return 地图记录数
     */
    @Query("SELECT COUNT(*) FROM map")
    fun getMapCount(): Int
} 
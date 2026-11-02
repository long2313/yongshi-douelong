package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.Equipment

/**
 * 装备数据访问接口
 */
@Dao
interface EquipmentDao {
    /**
     * 插入新装备
     * @param equipment 装备对象
     * @return 插入的装备ID
     */
    @Insert
    fun insert(equipment: Equipment): Long
    
    /**
     * 插入多个装备
     * @param equipments 装备列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(equipments: List<Equipment>): List<Long>
    
    /**
     * 更新装备信息
     * @param equipment 装备对象
     * @return 更新的行数
     */
    @Update
    fun update(equipment: Equipment): Int
    
    /**
     * 删除装备
     * @param equipment 装备对象
     * @return 删除的行数
     */
    @Delete
    fun delete(equipment: Equipment): Int
    
    /**
     * 根据ID删除装备
     * @param id 装备ID
     * @return 删除的行数
     */
    @Query("DELETE FROM equipment WHERE equipment_id = :id")
    fun deleteById(id: Long): Int
    
    /**
     * 根据ID获取装备
     * @param id 装备ID
     * @return 装备对象
     */
    @Query("SELECT * FROM equipment WHERE equipment_id = :id")
    fun findById(id: Long): Equipment?
    
    /**
     * 根据名称查找装备
     * @param name 装备名称
     * @return 装备对象
     */
    @Query("SELECT * FROM equipment WHERE name = :name LIMIT 1")
    fun findByName(name: String): Equipment?
    
    /**
     * 获取所有装备
     * @return 装备列表
     */
    @Query("SELECT * FROM equipment")
    fun findAll(): List<Equipment>
    
    /**
     * 获取所有装备（LiveData形式，可观察）
     * @return 装备LiveData列表
     */
    @Query("SELECT * FROM equipment")
    fun observeAll(): LiveData<List<Equipment>>
    
    /**
     * 根据类型获取装备
     * @param type 装备类型
     * @return 符合条件的装备列表
     */
    @Query("SELECT * FROM equipment WHERE type = :type")
    fun findByType(type: String): List<Equipment>
    
    /**
     * 获取指定稀有度的装备
     * @param rarity 稀有度
     * @return 符合条件的装备列表
     */
    @Query("SELECT * FROM equipment WHERE rarity = :rarity")
    fun findByRarity(rarity: String): List<Equipment>
    
    /**
     * 获取符合玩家等级的装备
     * @param playerLevel 玩家等级
     * @return 符合条件的装备列表
     */
    @Query("SELECT * FROM equipment WHERE level_required <= :playerLevel")
    fun findByPlayerLevel(playerLevel: Int): List<Equipment>
} 
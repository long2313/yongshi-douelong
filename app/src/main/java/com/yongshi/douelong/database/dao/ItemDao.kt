package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yongshi.douelong.database.model.Item

/**
 * 物品数据访问接口
 */
@Dao
interface ItemDao {
    /**
     * 插入新物品
     * @param item 物品对象
     * @return 插入的物品ID
     */
    @Insert
    fun insert(item: Item): Long
    
    /**
     * 插入多个物品
     * @param items 物品列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(items: List<Item>): List<Long>
    
    /**
     * 更新物品信息
     * @param item 物品对象
     * @return 更新的行数
     */
    @Update
    fun update(item: Item): Int
    
    /**
     * 删除物品
     * @param item 物品对象
     * @return 删除的行数
     */
    @Delete
    fun delete(item: Item): Int
    
    /**
     * 根据ID删除物品
     * @param id 物品ID
     * @return 删除的行数
     */
    @Query("DELETE FROM item WHERE item_id = :id")
    fun deleteById(id: Long): Int
    
    /**
     * 根据ID获取物品
     * @param id 物品ID
     * @return 物品对象
     */
    @Query("SELECT * FROM item WHERE item_id = :id")
    fun findById(id: Long): Item?
    
    /**
     * 根据名称查找物品
     * @param name 物品名称
     * @return 物品对象
     */
    @Query("SELECT * FROM item WHERE name = :name LIMIT 1")
    fun findByName(name: String): Item?
    
    /**
     * 获取所有物品
     * @return 物品列表
     */
    @Query("SELECT * FROM item")
    fun findAll(): List<Item>
    
    /**
     * 获取所有物品（LiveData形式，可观察）
     * @return 物品LiveData列表
     */
    @Query("SELECT * FROM item")
    fun observeAll(): LiveData<List<Item>>
    
    /**
     * 根据类型获取物品
     * @param type 物品类型
     * @return 符合条件的物品列表
     */
    @Query("SELECT * FROM item WHERE type = :type")
    fun findByType(type: String): List<Item>
    
    /**
     * 根据效果类型获取物品
     * @param effectType 效果类型
     * @return 符合条件的物品列表
     */
    @Query("SELECT * FROM item WHERE effect_type = :effectType")
    fun findByEffectType(effectType: String): List<Item>
    
    /**
     * 获取可堆叠物品
     * @param stackLimit 堆叠上限
     * @return 符合条件的物品列表
     */
    @Query("SELECT * FROM item WHERE stack_limit > :stackLimit")
    fun findStackableItems(stackLimit: Int = 1): List<Item>
} 
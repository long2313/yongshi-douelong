package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.MonsterDrop

/**
 * 怪物掉落数据访问接口
 */
@Dao
interface MonsterDropDao {
    /**
     * 插入新的怪物掉落
     * @param monsterDrop 怪物掉落对象
     * @return 插入的掉落ID
     */
    @Insert
    fun insert(monsterDrop: MonsterDrop): Long
    
    /**
     * 插入多个怪物掉落
     * @param monsterDrops 怪物掉落列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(monsterDrops: List<MonsterDrop>): List<Long>
    
    /**
     * 更新怪物掉落
     * @param monsterDrop 怪物掉落对象
     * @return 更新的行数
     */
    @Update
    fun update(monsterDrop: MonsterDrop): Int
    
    /**
     * 删除怪物掉落
     * @param monsterDrop 怪物掉落对象
     * @return 删除的行数
     */
    @Delete
    fun delete(monsterDrop: MonsterDrop): Int
    
    /**
     * 根据ID获取怪物掉落
     * @param id 掉落ID
     * @return 怪物掉落对象
     */
    @Query("SELECT * FROM monster_drop WHERE drop_id = :id")
    fun findById(id: Long): MonsterDrop?
    
    /**
     * 获取指定怪物的所有掉落
     * @param monsterId 怪物ID
     * @return 怪物掉落列表
     */
    @Query("SELECT * FROM monster_drop WHERE monster_id = :monsterId")
    fun findByMonsterId(monsterId: Long): List<MonsterDrop>
    
    /**
     * 获取指定怪物的所有掉落（LiveData形式，可观察）
     * @param monsterId 怪物ID
     * @return 怪物掉落LiveData列表
     */
    @Query("SELECT * FROM monster_drop WHERE monster_id = :monsterId")
    fun observeByMonsterId(monsterId: Long): LiveData<List<MonsterDrop>>
    
    /**
     * 获取可能掉落指定物品的怪物
     * @param itemId 物品ID
     * @return 可能掉落该物品的怪物掉落列表
     */
    @Query("SELECT * FROM monster_drop WHERE item_id = :itemId")
    fun findByItemId(itemId: Long): List<MonsterDrop>
    
    /**
     * 获取可能掉落指定装备的怪物
     * @param equipmentId 装备ID
     * @return 可能掉落该装备的怪物掉落列表
     */
    @Query("SELECT * FROM monster_drop WHERE equipment_id = :equipmentId")
    fun findByEquipmentId(equipmentId: Long): List<MonsterDrop>
    
    /**
     * 根据掉落率获取掉落物品
     * @param monsterId 怪物ID
     * @param minRate 最小掉落率
     * @return 符合条件的怪物掉落列表
     */
    @Query("SELECT * FROM monster_drop WHERE monster_id = :monsterId AND drop_rate >= :minRate")
    fun findByDropRate(monsterId: Long, minRate: Float): List<MonsterDrop>
} 
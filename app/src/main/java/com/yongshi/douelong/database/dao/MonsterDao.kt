package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.Monster

/**
 * 怪物数据访问接口
 */
@Dao
interface MonsterDao {
    /**
     * 插入新怪物
     * @param monster 怪物对象
     * @return 插入的怪物ID
     */
    @Insert
    fun insert(monster: Monster): Long
    
    /**
     * 插入多个怪物
     * @param monsters 怪物列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(monsters: List<Monster>): List<Long>
    
    /**
     * 更新怪物信息
     * @param monster 怪物对象
     * @return 更新的行数
     */
    @Update
    fun update(monster: Monster): Int
    
    /**
     * 删除怪物
     * @param monster 怪物对象
     * @return 删除的行数
     */
    @Delete
    fun delete(monster: Monster): Int
    
    /**
     * 根据ID删除怪物
     * @param id 怪物ID
     * @return 删除的行数
     */
    @Query("DELETE FROM monster WHERE monster_id = :id")
    fun deleteById(id: Long): Int
    
    /**
     * 根据ID获取怪物
     * @param id 怪物ID
     * @return 怪物对象
     */
    @Query("SELECT * FROM monster WHERE monster_id = :id")
    fun findById(id: Long): Monster?
    
    /**
     * 根据名称查找怪物
     * @param name 怪物名称
     * @return 怪物对象
     */
    @Query("SELECT * FROM monster WHERE name = :name LIMIT 1")
    fun findByName(name: String): Monster?
    
    /**
     * 获取所有怪物
     * @return 怪物列表
     */
    @Query("SELECT * FROM monster")
    fun findAll(): List<Monster>
    
    /**
     * 获取所有怪物（LiveData形式，可观察）
     * @return 怪物LiveData列表
     */
    @Query("SELECT * FROM monster")
    fun observeAll(): LiveData<List<Monster>>
    
    /**
     * 获取指定等级的怪物
     * @param level 等级
     * @return 符合条件的怪物列表
     */
    @Query("SELECT * FROM monster WHERE level = :level")
    fun findByLevel(level: Int): List<Monster>
    
    /**
     * 获取指定难度的怪物
     * @param difficulty 难度等级
     * @return 符合条件的怪物列表
     */
    @Query("SELECT * FROM monster WHERE difficulty = :difficulty")
    fun findByDifficulty(difficulty: String): List<Monster>
    
    /**
     * 获取Boss怪物
     * @return Boss怪物列表
     */
    @Query("SELECT * FROM monster WHERE is_boss = 1")
    fun findBosses(): List<Monster>

    @Query("SELECT COUNT(*) FROM monster")
    fun getMonsterCount(): Int

    @Query("""
        SELECT * FROM monster 
        WHERE difficulty = :difficulty 
        ORDER BY RANDOM() 
        LIMIT 1
    """)
    fun getRandomMonsterByDifficulty(difficulty: String): Monster

    @Query("SELECT * FROM monster WHERE difficulty = :difficulty")
    fun getMonstersByDifficulty(difficulty: String): List<Monster>
} 
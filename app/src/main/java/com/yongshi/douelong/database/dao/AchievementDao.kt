package com.yongshi.douelong.database.dao

import androidx.room.*
import com.yongshi.douelong.database.model.Achievement

/**
 * 成就数据访问接口
 */
@Dao
interface AchievementDao {
    /**
     * 插入新成就
     * @param achievement 成就对象
     * @return 插入的成就ID
     */
    @Insert
    fun insert(achievement: Achievement): Long
    
    /**
     * 批量插入成就
     * @param achievements 成就列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(achievements: List<Achievement>): List<Long>
    
    /**
     * 更新成就
     * @param achievement 成就对象
     * @return 更新的行数
     */
    @Update
    fun update(achievement: Achievement): Int
    
    /**
     * 删除成就
     * @param achievement 成就对象
     * @return 删除的行数
     */
    @Delete
    fun delete(achievement: Achievement): Int
    
    /**
     * 根据ID删除成就
     * @param achievementId 成就ID
     * @return 删除的行数
     */
    @Query("DELETE FROM achievement WHERE achievement_id = :achievementId")
    fun deleteById(achievementId: Long): Int
    
    /**
     * 根据ID查找成就
     * @param achievementId 成就ID
     * @return 成就对象
     */
    @Query("SELECT * FROM achievement WHERE achievement_id = :achievementId")
    fun findById(achievementId: Long): Achievement?
    
    /**
     * 根据名称查找成就
     * @param name 成就名称
     * @return 成就对象
     */
    @Query("SELECT * FROM achievement WHERE name = :name LIMIT 1")
    fun findByName(name: String): Achievement?
    
    /**
     * 获取所有成就
     * @return 所有成就列表
     */
    @Query("SELECT * FROM achievement ORDER BY name ASC")
    fun findAll(): List<Achievement>
    
    /**
     * 根据类别获取成就
     * @param category 成就类别
     * @return 该类别下的成就列表
     */
    @Query("SELECT * FROM achievement WHERE category = :category")
    fun findByCategory(category: String): List<Achievement>
} 
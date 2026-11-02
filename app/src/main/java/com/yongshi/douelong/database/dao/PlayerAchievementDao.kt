package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.Achievement
import com.yongshi.douelong.database.model.PlayerAchievement
import java.util.Date

/**
 * 玩家成就数据访问接口
 */
@Dao
interface PlayerAchievementDao {
    /**
     * 插入新的玩家成就关联
     * @param playerAchievement 玩家成就关联对象
     * @return 插入的关联ID
     */
    @Insert
    suspend fun insert(playerAchievement: PlayerAchievement): Long
    
    /**
     * 插入多个玩家成就关联
     * @param playerAchievements 玩家成就关联列表
     * @return 插入的ID列表
     */
    @Insert
    suspend fun insertAll(playerAchievements: List<PlayerAchievement>): List<Long>
    
    /**
     * 更新玩家成就关联
     * @param playerAchievement 玩家成就关联对象
     * @return 更新的行数
     */
    @Update
    suspend fun update(playerAchievement: PlayerAchievement): Int
    
    /**
     * 删除玩家成就关联
     * @param playerAchievement 玩家成就关联对象
     * @return 删除的行数
     */
    @Delete
    suspend fun delete(playerAchievement: PlayerAchievement): Int
    
    /**
     * 根据ID获取玩家成就关联
     * @param id 关联ID
     * @return 玩家成就关联对象
     */
    @Query("SELECT * FROM player_achievement WHERE player_achievement_id = :id")
    suspend fun findById(id: Long): PlayerAchievement?
    
    /**
     * 获取玩家所有成就
     * @param playerId 玩家ID
     * @return 玩家成就关联列表
     */
    @Query("SELECT * FROM player_achievement WHERE player_id = :playerId")
    suspend fun findByPlayerId(playerId: Long): List<PlayerAchievement>
    
    /**
     * 获取玩家所有成就（LiveData形式，可观察）
     * @param playerId 玩家ID
     * @return 玩家成就关联LiveData列表
     */
    @Query("SELECT * FROM player_achievement WHERE player_id = :playerId")
    fun observeByPlayerId(playerId: Long): LiveData<List<PlayerAchievement>>
    
    /**
     * 获取玩家已解锁但未领取的成就
     * @param playerId 玩家ID
     * @return 未领取成就列表
     */
    @Query("SELECT * FROM player_achievement WHERE player_id = :playerId AND progress >= 100 AND is_claimed = 0")
    suspend fun findUnclaimedAchievements(playerId: Long): List<PlayerAchievement>
    
    /**
     * 获取指定成就的所有玩家关联
     * @param achievementId 成就ID
     * @return 解锁该成就的玩家关联列表
     */
    @Query("SELECT * FROM player_achievement WHERE achievement_id = :achievementId")
    suspend fun findByAchievementId(achievementId: Long): List<PlayerAchievement>
    
    /**
     * 检查玩家是否已解锁指定成就
     * @param playerId 玩家ID
     * @param achievementId 成就ID
     * @return 玩家成就关联对象，如果不存在则为null
     */
    @Query("SELECT * FROM player_achievement WHERE player_id = :playerId AND achievement_id = :achievementId LIMIT 1")
    suspend fun findByPlayerAndAchievement(playerId: Long, achievementId: Long): PlayerAchievement?
    
    /**
     * 更新成就进度
     * @param playerAchievementId 玩家成就关联ID
     * @param progress 新的进度值
     * @return 更新的行数
     */
    @Query("UPDATE player_achievement SET progress = :progress WHERE player_achievement_id = :playerAchievementId")
    suspend fun updateProgress(playerAchievementId: Long, progress: Int): Int
    
    /**
     * 增加成就进度
     * @param playerAchievementId 玩家成就关联ID
     * @param amount 增加的数量
     * @return 更新的行数
     */
    @Query("UPDATE player_achievement SET progress = progress + :amount WHERE player_achievement_id = :playerAchievementId")
    suspend fun incrementProgress(playerAchievementId: Long, amount: Int): Int
    
    /**
     * 标记成就为已领取
     * @param playerAchievementId 玩家成就关联ID
     * @return 更新的行数
     */
    @Query("UPDATE player_achievement SET is_claimed = 1 WHERE player_achievement_id = :playerAchievementId")
    suspend fun markAsClaimed(playerAchievementId: Long): Int
    
    /**
     * 创建或更新成就进度
     * 如果成就已存在则更新进度，否则创建新记录
     * @param playerId 玩家ID
     * @param achievementId 成就ID
     * @param progress 进度值
     * @return 操作影响的行数
     */
    @Transaction
    suspend fun updateOrCreateAchievement(playerId: Long, achievementId: Long, progress: Int): Long {
        val existingAchievement = findByPlayerAndAchievement(playerId, achievementId)
        
        return if (existingAchievement != null) {
            updateProgress(existingAchievement.playerAchievementId!!, progress)
            existingAchievement.playerAchievementId
        } else {
            val newAchievement = PlayerAchievement(
                playerId = playerId,
                achievementId = achievementId,
                progress = progress,
                unlockDate = Date(),
                isClaimed = false
            )
            insert(newAchievement)
        }
    }
    
    /**
     * 获取玩家成就及其详细信息
     * @param playerId 玩家ID
     * @return 成就与关联状态详情
     */
    @Transaction
    @Query("""
        SELECT a.*, pa.player_achievement_id, pa.player_id, pa.progress, pa.unlock_date, pa.is_claimed
        FROM achievement a
        INNER JOIN player_achievement pa ON a.achievement_id = pa.achievement_id
        WHERE pa.player_id = :playerId
        ORDER BY pa.progress DESC, a.category, a.name
    """)
    suspend fun getPlayerAchievementDetails(playerId: Long): List<PlayerAchievementWithDetails>
    
    /**
     * 获取玩家已完成的成就详情
     * @param playerId 玩家ID
     * @return 已完成成就与关联状态详情
     */
    @Transaction
    @Query("""
        SELECT a.*, pa.player_achievement_id, pa.player_id, pa.progress, pa.unlock_date, pa.is_claimed
        FROM achievement a
        INNER JOIN player_achievement pa ON a.achievement_id = pa.achievement_id
        WHERE pa.player_id = :playerId AND pa.progress >= 100
        ORDER BY pa.unlock_date DESC
    """)
    suspend fun getCompletedAchievements(playerId: Long): List<PlayerAchievementWithDetails>
    
    /**
     * 获取玩家未完成的成就详情
     * @param playerId 玩家ID
     * @return 未完成成就与关联状态详情
     */
    @Transaction
    @Query("""
        SELECT a.*, pa.player_achievement_id, pa.player_id, pa.progress, pa.unlock_date, pa.is_claimed
        FROM achievement a
        INNER JOIN player_achievement pa ON a.achievement_id = pa.achievement_id
        WHERE pa.player_id = :playerId AND pa.progress < 100
        ORDER BY pa.progress DESC, a.category, a.name
    """)
    suspend fun getInProgressAchievements(playerId: Long): List<PlayerAchievementWithDetails>
    
    /**
     * 获取玩家未解锁的成就
     * @param playerId 玩家ID
     * @return 未解锁的成就列表
     */
    @Transaction
    @Query("""
        SELECT a.*
        FROM achievement a
        WHERE a.achievement_id NOT IN (
            SELECT achievement_id FROM player_achievement 
            WHERE player_id = :playerId
        )
        ORDER BY a.category, a.name
    """)
    suspend fun getUnlockedAchievements(playerId: Long): List<Achievement>
}

/**
 * 玩家成就详情视图类
 * 用于组合Achievement和PlayerAchievement的信息
 */
data class PlayerAchievementWithDetails(
    // 成就基本信息
    val achievement_id: Long,
    val name: String,
    val description: String,
    val category: String?,
    val icon_path: String?,
    val reward_type: String?,
    val reward_value: Int,
    
    // 关联信息
    val player_achievement_id: Long,
    val player_id: Long,
    val progress: Int,
    val unlock_date: Date,
    val is_claimed: Boolean
) 
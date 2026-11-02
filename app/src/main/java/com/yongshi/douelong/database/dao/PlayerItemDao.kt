package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.PlayerItem

/**
 * 玩家物品数据访问接口
 */
@Dao
interface PlayerItemDao {
    /**
     * 插入新的玩家物品关联
     * @param playerItem 玩家物品关联对象
     * @return 插入的关联ID
     */
    @Insert
    fun insert(playerItem: PlayerItem): Long
    
    /**
     * 插入多个玩家物品关联
     * @param playerItems 玩家物品关联列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(playerItems: List<PlayerItem>): List<Long>
    
    /**
     * 更新玩家物品关联
     * @param playerItem 玩家物品关联对象
     * @return 更新的行数
     */
    @Update
    fun update(playerItem: PlayerItem): Int
    
    /**
     * 删除玩家物品关联
     * @param playerItem 玩家物品关联对象
     * @return 删除的行数
     */
    @Delete
    fun delete(playerItem: PlayerItem): Int
    
    /**
     * 根据ID获取玩家物品关联
     * @param id 关联ID
     * @return 玩家物品关联对象
     */
    @Query("SELECT * FROM player_item WHERE player_item_id = :id")
    fun findById(id: Long): PlayerItem?
    
    /**
     * 获取玩家所有物品
     * @param playerId 玩家ID
     * @return 玩家物品关联列表
     */
    @Query("SELECT * FROM player_item WHERE player_id = :playerId")
    fun findByPlayerId(playerId: Long): List<PlayerItem>
    
    /**
     * 获取玩家所有物品（LiveData形式，可观察）
     * @param playerId 玩家ID
     * @return 玩家物品关联LiveData列表
     */
    @Query("SELECT * FROM player_item WHERE player_id = :playerId")
    fun observeByPlayerId(playerId: Long): LiveData<List<PlayerItem>>
    
    /**
     * 获取指定物品的所有玩家关联
     * @param itemId 物品ID
     * @return 拥有该物品的玩家关联列表
     */
    @Query("SELECT * FROM player_item WHERE item_id = :itemId")
    fun findByItemId(itemId: Long): List<PlayerItem>
    
    /**
     * 检查玩家是否拥有指定物品
     * @param playerId 玩家ID
     * @param itemId 物品ID
     * @return 玩家物品关联对象，如果不存在则为null
     */
    @Query("SELECT * FROM player_item WHERE player_id = :playerId AND item_id = :itemId LIMIT 1")
    fun findByPlayerAndItem(playerId: Long, itemId: Long): PlayerItem?
    
    /**
     * 更新玩家物品数量
     * @param playerItemId 玩家物品关联ID
     * @param amount 增加的数量（负数表示减少）
     * @return 更新的行数
     */
    @Query("UPDATE player_item SET quantity = quantity + :amount WHERE player_item_id = :playerItemId")
    fun updateQuantity(playerItemId: Long, amount: Int): Int
    
    /**
     * 更新或创建玩家物品
     * 如果玩家已有此物品，则增加数量；否则创建新记录
     * @param playerId 玩家ID
     * @param itemId 物品ID
     * @param amount 增加的数量
     * @return 更新/插入的行数
     */
    @Transaction
    fun addOrUpdateItem(playerId: Long, itemId: Long, amount: Int): Int {
        val existingItem = findByPlayerAndItem(playerId, itemId)
        return if (existingItem != null) {
            updateQuantity(existingItem.player_item_id, amount)
        } else {
            val newItem = PlayerItem(
                player_id = playerId,
                item_id = itemId,
                quantity = amount
            )
            insert(newItem)
            1
        }
    }
    
    /**
     * 删除数量为0的物品
     * @return 删除的行数
     */
    @Query("DELETE FROM player_item WHERE quantity <= 0")
    fun cleanupEmptyItems(): Int
} 
package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yongshi.douelong.database.model.GameSetting

/**
 * 游戏设置数据访问接口
 */
@Dao
interface GameSettingDao {
    /**
     * 插入新的游戏设置
     * @param gameSetting 游戏设置对象
     * @return 插入的设置ID
     */
    @Insert
    fun insert(gameSetting: GameSetting): Long
    
    /**
     * 更新游戏设置
     * @param gameSetting 游戏设置对象
     * @return 更新的行数
     */
    @Update
    fun update(gameSetting: GameSetting): Int
    
    /**
     * 删除游戏设置
     * @param gameSetting 游戏设置对象
     * @return 删除的行数
     */
    @Delete
    fun delete(gameSetting: GameSetting): Int
    
    /**
     * 根据ID获取游戏设置
     * @param id 设置ID
     * @return 游戏设置对象
     */
    @Query("SELECT * FROM game_setting WHERE setting_id = :id")
    fun findById(id: Long): GameSetting?
    
    /**
     * 根据玩家ID获取游戏设置
     * @param playerId 玩家ID
     * @return 游戏设置对象
     */
    @Query("SELECT * FROM game_setting WHERE player_id = :playerId")
    fun findByPlayerId(playerId: Long): GameSetting?
    
    /**
     * 根据玩家ID获取游戏设置（LiveData形式，可观察）
     * @param playerId 玩家ID
     * @return 游戏设置LiveData对象
     */
    @Query("SELECT * FROM game_setting WHERE player_id = :playerId")
    fun observeByPlayerId(playerId: Long): LiveData<GameSetting>
    
    /**
     * 更新音乐音量
     * @param playerId 玩家ID
     * @param volume 音量值（0-1）
     * @return 更新的行数
     */
    @Query("UPDATE game_setting SET music_volume = :volume WHERE player_id = :playerId")
    fun updateMusicVolume(playerId: Long, volume: Float): Int
    
    /**
     * 更新音效音量
     * @param playerId 玩家ID
     * @param volume 音量值（0-1）
     * @return 更新的行数
     */
    @Query("UPDATE game_setting SET sound_effect_volume = :volume WHERE player_id = :playerId")
    fun updateSoundEffectVolume(playerId: Long, volume: Float): Int
    
    /**
     * 更新文字速度
     * @param playerId 玩家ID
     * @param speed 速度值（1-5）
     * @return 更新的行数
     */
    @Query("UPDATE game_setting SET text_speed = :speed WHERE player_id = :playerId")
    fun updateTextSpeed(playerId: Long, speed: Int): Int
    
    /**
     * 更新游戏难度
     * @param playerId 玩家ID
     * @param difficulty 难度（easy/normal/hard）
     * @return 更新的行数
     */
    @Query("UPDATE game_setting SET difficulty = :difficulty WHERE player_id = :playerId")
    fun updateDifficulty(playerId: Long, difficulty: String): Int
    
    /**
     * 更新游戏语言
     * @param playerId 玩家ID
     * @param language 语言代码
     * @return 更新的行数
     */
    @Query("UPDATE game_setting SET language = :language WHERE player_id = :playerId")
    fun updateLanguage(playerId: Long, language: String): Int
    
    /**
     * 更新震动设置
     * @param playerId 玩家ID
     * @param enabled 是否启用
     * @return 更新的行数
     */
    @Query("UPDATE game_setting SET vibration_enabled = :enabled WHERE player_id = :playerId")
    fun updateVibrationEnabled(playerId: Long, enabled: Boolean): Int
    
    /**
     * 更新通知设置
     * @param playerId 玩家ID
     * @param enabled 是否启用
     * @return 更新的行数
     */
    @Query("UPDATE game_setting SET notification_enabled = :enabled WHERE player_id = :playerId")
    fun updateNotificationEnabled(playerId: Long, enabled: Boolean): Int
} 
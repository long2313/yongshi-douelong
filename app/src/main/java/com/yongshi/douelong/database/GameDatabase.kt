package com.yongshi.douelong.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yongshi.douelong.database.converter.DateConverter
import com.yongshi.douelong.database.dao.AchievementDao
import com.yongshi.douelong.database.dao.BattleRecordDao
import com.yongshi.douelong.database.dao.BattleRoundDao
import com.yongshi.douelong.database.dao.ChoiceQuestionDao
import com.yongshi.douelong.database.dao.SpanishQuestionDao
import com.yongshi.douelong.database.dao.EquipmentDao
import com.yongshi.douelong.database.dao.FillBlankQuestionDao
import com.yongshi.douelong.database.dao.GameSaveDao
import com.yongshi.douelong.database.dao.GameSettingDao
import com.yongshi.douelong.database.dao.ItemDao
import com.yongshi.douelong.database.dao.MapDao
import com.yongshi.douelong.database.dao.MapNodeDao
import com.yongshi.douelong.database.dao.MonsterDao
import com.yongshi.douelong.database.dao.MonsterDropDao
import com.yongshi.douelong.database.dao.MonsterQuestionDao
import com.yongshi.douelong.database.dao.NodeConnectionDao
import com.yongshi.douelong.database.dao.NodeEventDao
import com.yongshi.douelong.database.dao.PlayerAchievementDao
import com.yongshi.douelong.database.dao.PlayerDao
import com.yongshi.douelong.database.dao.PlayerEquipmentDao
import com.yongshi.douelong.database.dao.PlayerItemDao
import com.yongshi.douelong.database.model.Achievement
import com.yongshi.douelong.database.model.BattleRecord
import com.yongshi.douelong.database.model.BattleRound
import com.yongshi.douelong.database.model.ChoiceQuestion
import com.yongshi.douelong.database.model.SpanishQuestion
import com.yongshi.douelong.database.model.Equipment
import com.yongshi.douelong.database.model.FillBlankQuestion
import com.yongshi.douelong.database.model.GameSave
import com.yongshi.douelong.database.model.GameSetting
import com.yongshi.douelong.database.model.Item
import com.yongshi.douelong.database.model.Map
import com.yongshi.douelong.database.model.MapNode
import com.yongshi.douelong.database.model.Monster
import com.yongshi.douelong.database.model.MonsterDrop
import com.yongshi.douelong.database.model.MonsterQuestion
import com.yongshi.douelong.database.model.NodeConnection
import com.yongshi.douelong.database.model.NodeEvent
import com.yongshi.douelong.database.model.Player
import com.yongshi.douelong.database.model.PlayerAchievement
import com.yongshi.douelong.database.model.PlayerEquipment
import com.yongshi.douelong.database.model.PlayerItem
import com.yongshi.douelong.database.util.SqlScriptExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 游戏数据库类 - 使用Room
 */
@Database(
    entities = [
        Player::class,
        Equipment::class,
        Item::class,
        PlayerEquipment::class,
        PlayerItem::class,
        Monster::class,
        MonsterDrop::class,
        SpanishQuestion::class,
        FillBlankQuestion::class,
        ChoiceQuestion::class,
        MonsterQuestion::class,
        Map::class,
        MapNode::class,
        NodeConnection::class,
        NodeEvent::class,
        BattleRecord::class,
        BattleRound::class,
        GameSave::class,
        GameSetting::class,
        Achievement::class,
        PlayerAchievement::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class GameDatabase : RoomDatabase() {
    // 存储Context引用
    internal var context: Context? = null
        private set

    // DAO抽象方法
    abstract fun playerDao(): PlayerDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun itemDao(): ItemDao
    abstract fun playerEquipmentDao(): PlayerEquipmentDao
    abstract fun playerItemDao(): PlayerItemDao
    abstract fun monsterDao(): MonsterDao
    abstract fun monsterDropDao(): MonsterDropDao
    abstract fun spanishQuestionDao(): SpanishQuestionDao
    abstract fun fillBlankQuestionDao(): FillBlankQuestionDao
    abstract fun choiceQuestionDao(): ChoiceQuestionDao
    abstract fun monsterQuestionDao(): MonsterQuestionDao
    abstract fun mapDao(): MapDao
    abstract fun mapNodeDao(): MapNodeDao
    abstract fun nodeConnectionDao(): NodeConnectionDao
    abstract fun nodeEventDao(): NodeEventDao
    abstract fun battleRecordDao(): BattleRecordDao
    abstract fun battleRoundDao(): BattleRoundDao
    abstract fun gameSaveDao(): GameSaveDao
    abstract fun gameSettingDao(): GameSettingDao
    abstract fun achievementDao(): AchievementDao
    abstract fun playerAchievementDao(): PlayerAchievementDao
    
    companion object {
        private const val TAG = "GameDatabase"
        
        // 单例防止同时打开多个数据库实例
        @Volatile
        private var INSTANCE: GameDatabase? = null
        
        fun getDatabase(context: Context, scope: CoroutineScope): GameDatabase {
            // 如果INSTANCE不为null，则返回它，否则创建数据库
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "adventure_game.db"
                )
                .addCallback(GameDatabaseCallback(scope))
                // 允许主线程查询，通常不推荐，但游戏中有时需要
                .allowMainThreadQueries()
                // 如果需要从旧版SQLite迁移数据，可以添加迁移策略
                .fallbackToDestructiveMigration()
                .build()
                
                // 设置context
                instance.context = context.applicationContext
                INSTANCE = instance
                instance
            }
        }
        
        // 数据库回调
        private class GameDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.i(TAG, "数据库创建完成")
                
                // 在协程中执行SQL脚本
                scope.launch(Dispatchers.IO) {
                    try {
                        // 执行问题插入SQL脚本
                        INSTANCE?.let { database ->
                            database.context?.let { context ->
                                SqlScriptExecutor.executeFromAssets(context, db, "question_inserts.sql")
                                Log.i(TAG, "问题数据初始化完成")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "执行SQL脚本失败: ${e.message}")
                    }
                }
            }
        }
    }
} 
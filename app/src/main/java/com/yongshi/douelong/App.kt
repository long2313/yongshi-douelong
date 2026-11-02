package com.yongshi.douelong

import android.app.Application
import android.util.Log
import com.yongshi.douelong.database.GameDatabase
import com.yongshi.douelong.database.model.Map
import com.yongshi.douelong.database.model.MapNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking

/**
 * 应用类
 * 用于初始化全局资源，如数据库
 */
class App : Application() {
    // 应用级别的协程作用域
    private val applicationScope = CoroutineScope(SupervisorJob())

    // 延迟初始化数据库实例
    val database by lazy {
        GameDatabase.getDatabase(this, applicationScope)
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "应用程序启动")

        // 启动时同步初始化地图与节点数据（幂等：已有数据则跳过）
        initMapAndNodeDataSync()
    }

    /**
     * 同步初始化默认地图与地图节点。
     *
     * 与原版「每次创建角色时随机生成世界」不同，这里在应用启动时一次性写入
     * 固定的三张地图与三个节点，之后创建角色直接复用，保证存档/关卡稳定可复现。
     */
    private fun initMapAndNodeDataSync() {
        runBlocking(Dispatchers.IO) {
            try {
                // ---------- 地图 ----------
                val mapDao = database.mapDao()
                val mapCount = mapDao.getMapCount()
                Log.i(TAG, "地图表当前记录数：$mapCount")
                if (mapCount == 0) {
                    Log.i(TAG, "地图表为空，开始初始化默认地图数据")
                    val maps = listOf(
                        Map(1, "知识之岛", "新手入门的西语冒险岛", 1, true),
                        Map(2, "智慧群山", "进阶语法挑战区", 5, true),
                        Map(3, "学习谷地", "词汇积累专属地图", 3, true)
                    )
                    mapDao.insertAll(maps)
                    Log.i(TAG, "默认地图初始化完成，共插入${maps.size}条")
                } else {
                    Log.i(TAG, "地图表已有数据，跳过初始化")
                }

                // ---------- 地图节点 ----------
                val mapNodeDao = database.mapNodeDao()
                val nodeCount = mapNodeDao.getNodeCount()
                Log.i(TAG, "地图节点表当前记录数：$nodeCount")
                if (nodeCount == 0) {
                    Log.i(TAG, "地图节点表为空，开始初始化默认节点数据")
                    val nodes = listOf(
                        MapNode(1, 1, "新手村广场", "知识之岛的初始节点，适合新手起步", "NORMAL", 0, 0, true, false),
                        MapNode(2, 1, "语法森林", "挑战西班牙语语法题的战斗区域", "BATTLE", 10, 5, false, false),
                        MapNode(3, 2, "词汇山峰", "进阶词汇挑战的BOSS区域", "BOSS", 20, 15, false, true)
                    )
                    mapNodeDao.insertAll(nodes)
                    Log.i(TAG, "默认地图节点初始化完成，共插入${nodes.size}条")
                } else {
                    Log.i(TAG, "地图节点表已有数据，跳过初始化")
                }
            } catch (e: Exception) {
                Log.e(TAG, "地图/节点初始化失败：${e.message}", e)
            }
        }
    }

    companion object {
        private const val TAG = "GameApp"
    }
}

package com.yongshi.douelong.database

import android.util.Log
import com.yongshi.douelong.database.model.MapNode
import com.yongshi.douelong.database.model.Map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * 游戏世界生成器
 * 负责生成新的游戏地图、节点和其他游戏元素
 */
class GameWorldGenerator(private val database: GameDatabase) {
    companion object {
        private const val TAG = "GameWorldGenerator"
    }

    /**
     * 生成新的游戏世界
     * @return Pair<Long, Long> 返回地图ID和起始节点ID
     */
    suspend fun generateNewWorld(): Pair<Long, Long> = withContext(Dispatchers.IO) {
        try {
            Log.i(TAG, "开始生成新的游戏世界")
            
            // 生成新地图
            val mapDao = database.mapDao()
            val mapNodeDao = database.mapNodeDao()
            
            // 随机生成地图名称
            val mapNames = listOf(
                "翡翠森林大陆",
                "知识之岛",
                "智慧群山",
                "学习谷地",
                "探索海岸"
            )
            val mapDescriptions = listOf(
                "这是一片充满魔法和知识的大陆，这里的居民重视学习和智慧的力量。",
                "一个被知识之海环绕的神秘岛屿，蕴含着无尽的智慧。",
                "巍峨的群山之间，藏着众多知识的宝藏等待发掘。",
                "在这片宁静的谷地中，处处都是学习的机会。",
                "海浪拍打着知识的海岸，每一天都有新的发现。"
            )
            
            val randomIndex = Random.nextInt(mapNames.size)
            val newMap = Map(
                map_id = 0,
                name = mapNames[randomIndex],
                description = mapDescriptions[randomIndex],
                level_required = 1,
                is_active = true
            )
            val mapId = mapDao.insert(newMap)
            
            // 生成起始村庄
            val villageNames = listOf(
                "知识村",
                "启智镇",
                "学习城",
                "智慧村",
                "探索镇"
            )
            val villageDescriptions = listOf(
                "这是一个宁静而充满智慧的村庄，这里的人们热爱学习，尤其是对英语的学习有着浓厚的兴趣。",
                "镇上的居民都相信知识就是力量，他们世代传承着对学习的热爱。",
                "一座充满活力的小城，到处都能听到朗朗的读书声。",
                "村民们相信只有通过不断学习，才能成为真正的勇者。",
                "这里的每个人都怀着探索未知的热情，渴望获取新的知识。"
            )
            
            val villageIndex = Random.nextInt(villageNames.size)
            val startingNode = MapNode(
                node_id = 0,
                map_id = mapId,
                name = villageNames[villageIndex],
                description = villageDescriptions[villageIndex],
                node_type = "VILLAGE",
                x_coordinate = 0,
                y_coordinate = 0,
                is_starting_point = true,
                is_dragon_lair = false
            )
            val startingNodeId = mapNodeDao.insert(startingNode)
            
            // 生成Boss巢穴和守卫节点
            createRandomBossLair(mapId)
            
            Log.i(TAG, "新的游戏世界生成完成")
            Pair(mapId, startingNodeId)
        } catch (e: Exception) {
            Log.e(TAG, "生成游戏世界失败: ${e.message}")
            throw e
        }
    }

    /**
     * 创建随机位置的Boss巢穴及其守卫节点
     */
    private suspend fun createRandomBossLair(mapId: Long) = withContext(Dispatchers.IO) {
        try {
            val mapNodeDao = database.mapNodeDao()
            val nodeConnectionDao = database.nodeConnectionDao()
            
            // 随机选择Boss巢穴坐标 (距离起点较远)
            val bossX = (10..49).random()
            val bossY = (10..49).random()
            
            // 创建Boss巢穴节点
            val bossNode = MapNode(
                node_id = 0,
                map_id = mapId,
                name = "恶龙巢穴",
                description = "这里是恶龙的巢穴，空气中弥漫着硫磺的气息。巨大的龙爪痕迹和烧焦的痕迹随处可见。地面上散落着骨头和宝藏残骸。一条巨大的恶龙盘踞在宝藏堆上，注视着你的一举一动。",
                node_type = "BOSS",
                x_coordinate = bossX,
                y_coordinate = bossY,
                is_starting_point = false,
                is_dragon_lair = true
            )
            val bossNodeId = mapNodeDao.insert(bossNode)
            
            // 为Boss巢穴添加守卫节点
//            val guardianDirections = listOf("北方", "东北方", "东方", "东南方", "南方", "西南方", "西方", "西北方")
//            val randomGuardians = guardianDirections.shuffled().take((1..3).random())
//
//            for (direction in randomGuardians) {
//                val (guardX, guardY) = calculateNewCoordinates(bossX, bossY, direction)
//
//                val guardianNode = MapNode(
//                    node_id = 0,
//                    map_id = mapId,
//                    name = "危险区域",
//                    description = "这片区域充满了危险的气息，似乎有什么强大的生物在附近。地面上有奇怪的痕迹，空气中弥漫着一丝硫磺的味道。",
//                    node_type = "DANGER",
//                    x_coordinate = guardX,
//                    y_coordinate = guardY,
//                    is_starting_point = false,
//                    is_dragon_lair = false
//                )
//                val guardianNodeId = mapNodeDao.insert(guardianNode)
//
//                // 创建从守卫节点到Boss巢穴的连接
//                val guardianToBoss = NodeConnection(
//                    connection_id = 0,
//                    from_node_id = guardianNodeId,
//                    to_node_id = bossNodeId,
//                    direction = getOppositeDirection(direction),
//                    is_locked = true,
//                    unlock_condition = "需要解决谜题或击败守卫"
//                )
//                nodeConnectionDao.insert(guardianToBoss)
//            }
        } catch (e: Exception) {
            Log.e(TAG, "创建Boss巢穴失败: ${e.message}")
            throw e
        }
    }

    /**
     * 根据基准坐标和方向计算新坐标
     */
    private fun calculateNewCoordinates(baseX: Int, baseY: Int, direction: String): Pair<Int, Int> {
        return when (direction) {
            "北方" -> Pair(baseX, baseY - 1)
            "东北方" -> Pair(baseX + 1, baseY - 1)
            "东方" -> Pair(baseX + 1, baseY)
            "东南方" -> Pair(baseX + 1, baseY + 1)
            "南方" -> Pair(baseX, baseY + 1)
            "西南方" -> Pair(baseX - 1, baseY + 1)
            "西方" -> Pair(baseX - 1, baseY)
            "西北方" -> Pair(baseX - 1, baseY - 1)
            else -> Pair(baseX, baseY)
        }
    }

    /**
     * 获取相反的方向
     */
    private fun getOppositeDirection(direction: String): String {
        return when (direction) {
            "北方" -> "南方"
            "东北方" -> "西南方"
            "东方" -> "西方"
            "东南方" -> "西北方"
            "南方" -> "北方"
            "西南方" -> "东北方"
            "西方" -> "东方"
            "西北方" -> "东南方"
            else -> "未知方向"
        }
    }
} 
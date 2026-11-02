package com.yongshi.douelong.database.dao

import androidx.room.*
import com.yongshi.douelong.database.model.MapNode

/**
 * 地图节点数据访问接口
 */
@Dao
interface MapNodeDao {
    /**
     * 插入新节点
     * @param mapNode 地图节点对象
     * @return 插入的节点ID
     */
    @Insert
    fun insert(mapNode: MapNode): Long
    
    /**
     * 批量插入节点
     * @param mapNodes 地图节点列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(mapNodes: List<MapNode>): List<Long>
    
    /**
     * 更新节点
     * @param mapNode 地图节点对象
     * @return 更新的行数
     */
    @Update
    fun update(mapNode: MapNode): Int
    
    /**
     * 删除节点
     * @param mapNode 地图节点对象
     * @return 删除的行数
     */
    @Delete
    fun delete(mapNode: MapNode): Int
    
    /**
     * 根据ID删除节点
     * @param nodeId 节点ID
     * @return 删除的行数
     */
    @Query("DELETE FROM map_node WHERE node_id = :nodeId")
    fun deleteById(nodeId: Long): Int
    
    /**
     * 根据ID查找节点
     * @param nodeId 节点ID
     * @return 地图节点对象
     */
    @Query("SELECT * FROM map_node WHERE node_id = :nodeId")
    fun findById(nodeId: Long): MapNode?
    
    /**
     * 查找所有节点
     * @return 所有地图节点列表
     */
    @Query("SELECT * FROM map_node")
    fun findAll(): List<MapNode>
    
    /**
     * 根据地图ID查找节点
     * @param mapId 地图ID
     * @return 该地图的所有节点
     */
    @Query("SELECT * FROM map_node WHERE map_id = :mapId")
    fun findByMapId(mapId: Long): List<MapNode>
    
    /**
     * 查找特定类型的节点
     * @param nodeType 节点类型
     * @return 特定类型的节点列表
     */
    @Query("SELECT * FROM map_node WHERE node_type = :nodeType")
    fun findByNodeType(nodeType: String): List<MapNode>
    
    /**
     * 查找地图上的起始点
     * @param mapId 地图ID
     * @return 起始点节点
     */
    @Query("SELECT * FROM map_node WHERE map_id = :mapId AND is_starting_point = 1 LIMIT 1")
    fun findStartingNode(mapId: Long): MapNode?
    
    /**
     * 查找地图上的龙巢穴
     * @param mapId 地图ID
     * @return 龙巢穴节点
     */
    @Query("SELECT * FROM map_node WHERE map_id = :mapId AND is_dragon_lair = 1 LIMIT 1")
    fun findDragonLair(mapId: Long): MapNode?
    
    /**
     * 根据坐标查找节点
     * @param mapId 地图ID
     * @param x X坐标
     * @param y Y坐标
     * @return 符合坐标的节点
     */
    @Query("SELECT * FROM map_node WHERE map_id = :mapId AND x_coordinate = :x AND y_coordinate = :y LIMIT 1")
    fun findByCoordinates(mapId: Long, x: Int, y: Int): MapNode?

    /**
     * 统计节点总数（用于启动时判断是否需要初始化默认节点）
     * @return 节点记录数
     */
    @Query("SELECT COUNT(*) FROM map_node")
    fun getNodeCount(): Int
} 
package com.yongshi.douelong.database.dao

import androidx.room.*
import com.yongshi.douelong.database.model.NodeConnection

/**
 * 节点连接数据访问接口
 */
@Dao
interface NodeConnectionDao {
    /**
     * 插入新连接
     * @param connection 节点连接对象
     * @return 插入的连接ID
     */
    @Insert
    fun insert(connection: NodeConnection): Long
    
    /**
     * 批量插入连接
     * @param connections 连接列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(connections: List<NodeConnection>): List<Long>
    
    /**
     * 更新连接
     * @param connection 节点连接对象
     * @return 更新的行数
     */
    @Update
    fun update(connection: NodeConnection): Int
    
    /**
     * 删除连接
     * @param connection 节点连接对象
     * @return 删除的行数
     */
    @Delete
    fun delete(connection: NodeConnection): Int
    
    /**
     * 根据ID删除连接
     * @param connectionId 连接ID
     * @return 删除的行数
     */
    @Query("DELETE FROM node_connection WHERE connection_id = :connectionId")
    fun deleteById(connectionId: Long): Int
    
    /**
     * 根据ID查找连接
     * @param connectionId 连接ID
     * @return 节点连接对象
     */
    @Query("SELECT * FROM node_connection WHERE connection_id = :connectionId")
    fun findById(connectionId: Long): NodeConnection?
    
    /**
     * 获取所有连接
     * @return 所有节点连接列表
     */
    @Query("SELECT * FROM node_connection")
    fun findAll(): List<NodeConnection>
    
    /**
     * 获取从特定节点出发的连接
     * @param nodeId 起始节点ID
     * @return 从该节点出发的连接列表
     */
    @Query("SELECT * FROM node_connection WHERE from_node_id = :nodeId")
    fun findConnectionsFromNode(nodeId: Long): List<NodeConnection>
    
    /**
     * 获取到达特定节点的连接
     * @param nodeId 目标节点ID
     * @return 到达该节点的连接列表
     */
    @Query("SELECT * FROM node_connection WHERE to_node_id = :nodeId")
    fun findConnectionsToNode(nodeId: Long): List<NodeConnection>
    
    /**
     * 获取两个节点之间的连接
     * @param fromNodeId 起始节点ID
     * @param toNodeId 目标节点ID
     * @return 节点间的连接对象
     */
    @Query("SELECT * FROM node_connection WHERE from_node_id = :fromNodeId AND to_node_id = :toNodeId LIMIT 1")
    fun findConnectionBetweenNodes(fromNodeId: Long, toNodeId: Long): NodeConnection?
    
    /**
     * 解锁连接
     * @param connectionId 连接ID
     * @return 更新的行数
     */
    @Query("UPDATE node_connection SET is_locked = 0 WHERE connection_id = :connectionId")
    fun unlockConnection(connectionId: Long): Int
    
    /**
     * 锁定连接
     * @param connectionId 连接ID
     * @param unlockCondition 解锁条件
     * @return 更新的行数
     */
    @Query("UPDATE node_connection SET is_locked = 1, unlock_condition = :unlockCondition WHERE connection_id = :connectionId")
    fun lockConnection(connectionId: Long, unlockCondition: String?): Int
} 
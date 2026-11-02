package com.yongshi.douelong.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yongshi.douelong.database.model.NodeEvent

/**
 * 节点事件数据访问接口
 */
@Dao
interface NodeEventDao {
    /**
     * 插入新的节点事件
     * @param nodeEvent 节点事件对象
     * @return 插入的事件ID
     */
    @Insert
    fun insert(nodeEvent: NodeEvent): Long
    
    /**
     * 插入多个节点事件
     * @param nodeEvents 节点事件列表
     * @return 插入的ID列表
     */
    @Insert
    fun insertAll(nodeEvents: List<NodeEvent>): List<Long>
    
    /**
     * 更新节点事件
     * @param nodeEvent 节点事件对象
     * @return 更新的行数
     */
    @Update
    fun update(nodeEvent: NodeEvent): Int
    
    /**
     * 删除节点事件
     * @param nodeEvent 节点事件对象
     * @return 删除的行数
     */
    @Delete
    fun delete(nodeEvent: NodeEvent): Int
    
    /**
     * 根据ID获取节点事件
     * @param id 事件ID
     * @return 节点事件对象
     */
    @Query("SELECT * FROM node_event WHERE event_id = :id")
    fun findById(id: Long): NodeEvent?
    
    /**
     * 获取指定节点的所有事件
     * @param nodeId 节点ID
     * @return 节点事件列表
     */
    @Query("SELECT * FROM node_event WHERE node_id = :nodeId")
    fun findByNodeId(nodeId: Long): List<NodeEvent>
    
    /**
     * 获取指定节点的所有事件（LiveData形式，可观察）
     * @param nodeId 节点ID
     * @return 节点事件LiveData列表
     */
    @Query("SELECT * FROM node_event WHERE node_id = :nodeId")
    fun observeByNodeId(nodeId: Long): LiveData<List<NodeEvent>>
    
    /**
     * 根据事件类型获取节点事件
     * @param eventType 事件类型
     * @return 节点事件列表
     */
    @Query("SELECT * FROM node_event WHERE event_type = :eventType")
    fun findByEventType(eventType: String): List<NodeEvent>
    
    /**
     * 获取指定节点的特定类型事件
     * @param nodeId 节点ID
     * @param eventType 事件类型
     * @return 节点事件列表
     */
    @Query("SELECT * FROM node_event WHERE node_id = :nodeId AND event_type = :eventType")
    fun findByNodeIdAndEventType(nodeId: Long, eventType: String): List<NodeEvent>
    
    /**
     * 获取一次性事件
     * @return 一次性事件列表
     */
    @Query("SELECT * FROM node_event WHERE is_one_time = 1")
    fun findOneTimeEvents(): List<NodeEvent>
    
    /**
     * 获取非一次性事件
     * @return 非一次性事件列表
     */
    @Query("SELECT * FROM node_event WHERE is_one_time = 0")
    fun findRepeatingEvents(): List<NodeEvent>
    
    /**
     * 根据触发概率获取事件
     * @param minProbability 最小触发概率
     * @return 符合条件的事件列表
     */
    @Query("SELECT * FROM node_event WHERE trigger_probability >= :minProbability")
    fun findByMinTriggerProbability(minProbability: Float): List<NodeEvent>
    
    /**
     * 更新事件触发条件
     * @param eventId 事件ID
     * @param triggerCondition 触发条件
     * @return 更新的行数
     */
    @Query("UPDATE node_event SET trigger_condition = :triggerCondition WHERE event_id = :eventId")
    fun updateTriggerCondition(eventId: Long, triggerCondition: String): Int
} 
package com.yongshi.douelong.database.converter

import androidx.room.TypeConverter
import java.util.Date

/**
 * Room数据库日期转换器
 * 用于在数据库Long类型和Java Date类型之间转换
 */
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
} 
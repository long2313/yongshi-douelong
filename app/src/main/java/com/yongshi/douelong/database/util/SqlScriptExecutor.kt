package com.yongshi.douelong.database.util

import android.content.Context
import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * SQL脚本执行工具类
 */
object SqlScriptExecutor {
    private const val TAG = "SqlScriptExecutor"
    
    /**
     * 从assets文件夹读取并执行SQL脚本
     * @param context 上下文
     * @param db 数据库实例
     * @param scriptFileName 脚本文件名
     */
    fun executeFromAssets(context: Context, db: SupportSQLiteDatabase, scriptFileName: String) {
        try {
            Log.i(TAG, "开始执行SQL脚本: $scriptFileName")
            
            // 从assets读取SQL文件
            context.assets.open(scriptFileName).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    val script = StringBuilder()
                    var line: String?
                    
                    // 读取每一行
                    while (reader.readLine().also { line = it } != null) {
                        // 忽略注释和空行
                        if (line!!.trim().startsWith("--") || line!!.trim().isEmpty()) {
                            continue
                        }
                        script.append(line).append("\n")
                        
                        // 如果遇到分号，说明是一条完整的SQL语句
                        if (line!!.trim().endsWith(";")) {
                            val sql = script.toString().trim()
                            if (sql.isNotEmpty()) {
                                try {
                                    db.execSQL(sql)
                                } catch (e: Exception) {
                                    Log.e(TAG, "执行SQL语句失败: $sql", e)
                                }
                            }
                            script.clear()
                        }
                    }
                }
            }
            
            Log.i(TAG, "SQL脚本执行完成: $scriptFileName")
        } catch (e: Exception) {
            Log.e(TAG, "执行SQL脚本失败: $scriptFileName", e)
            throw e
        }
    }
} 
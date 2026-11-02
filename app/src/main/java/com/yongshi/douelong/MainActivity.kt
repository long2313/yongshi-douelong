package com.yongshi.douelong

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yongshi.douelong.database.dao.GameSaveDao
import com.yongshi.douelong.database.model.GameSave
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var gameSaveDao: GameSaveDao
    private val TAG = "MainActivity"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // 初始化数据库
        initDatabase()
        
        // 设置按钮点击事件
        setupButtonListeners()
    }
    
    /**
     * 初始化数据库
     */
    private fun initDatabase() {
        try {
            Log.i(TAG, "初始化数据库DAO")
            gameSaveDao = (application as App).database.gameSaveDao()
        } catch (e: Exception) {
            Log.e(TAG, "数据库初始化失败: ${e.message}")
            Toast.makeText(this, "游戏数据库初始化失败", Toast.LENGTH_LONG).show()
        }
    }
    
    /**
     * 设置按钮点击事件监听
     */
    private fun setupButtonListeners() {
        // 开始游戏按钮
        findViewById<Button>(R.id.btnStartGame).setOnClickListener {
            try {
                startActivity(Intent(this, GameActivity::class.java))
            } catch (e: Exception) {
                Log.e(TAG, "启动游戏活动失败: ${e.message}")
                Toast.makeText(this, "启动游戏失败", Toast.LENGTH_LONG).show()
            }
        }
        
        // 继续游戏按钮
        findViewById<Button>(R.id.btnContinueGame).setOnClickListener {
            loadSavedGames()
        }
        
        // 设置按钮
        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            Toast.makeText(this, "打开设置", Toast.LENGTH_SHORT).show()
            // TODO: 实现打开设置的逻辑
        }
        
        // 退出按钮
        findViewById<Button>(R.id.btnExit).setOnClickListener {
            finish() // 关闭当前Activity，退出应用
        }
    }
    
    /**
     * 加载已保存的游戏列表
     */
    private fun loadSavedGames() {
        try {
            // 从数据库获取所有存档
            launch {
                val savedGames = gameSaveDao.findAll()
                
                if (savedGames.isEmpty()) {
                    Toast.makeText(this@MainActivity, "没有找到保存的游戏", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                
                // 在主线程上显示对话框
                runOnUiThread {
                    // 创建并显示新的存档列表对话框
                    val dialogView = LayoutInflater.from(this@MainActivity)
                        .inflate(R.layout.dialog_save_list, null)
                    
                    val dialog = AlertDialog.Builder(this@MainActivity)
                        .setTitle("选择存档")
                        .setView(dialogView)
                        .create()
                    
                    // 获取视图引用
                    val saveItemsContainer = dialogView.findViewById<LinearLayout>(R.id.saveItemsContainer)
                    
                    // 添加存档项
                    savedGames.forEach { save ->
                        val saveItemView = LayoutInflater.from(this@MainActivity)
                            .inflate(R.layout.item_save, saveItemsContainer, false)
                        
                        // 设置存档信息
                        saveItemView.findViewById<TextView>(R.id.saveNameText).text = save.save_name
                        saveItemView.findViewById<TextView>(R.id.saveTimeText).text = 
                            "保存时间：${save.save_time}"
                        saveItemView.findViewById<TextView>(R.id.saveInfoText).text = 
                            "存档信息：${save.player_state ?: "无"}"
                        
                        // 设置存档项点击事件（加载存档）
                        saveItemView.setOnClickListener {
                            dialog.dismiss()
                            continueGame(save)
                        }

                        // 设置删除按钮点击事件
                        saveItemView.findViewById<Button>(R.id.deleteButton).setOnClickListener {
                            confirmDeleteSave(save, dialog)
                        }
                        
                        saveItemsContainer.addView(saveItemView)
                    }
                    
                    dialog.show()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "加载存档列表失败: ${e.message}")
            Toast.makeText(this, "加载存档列表失败", Toast.LENGTH_LONG).show()
        }
    }
    
    /**
     * 确认删除存档
     */
    private fun confirmDeleteSave(save: GameSave, parentDialog: AlertDialog) {
        AlertDialog.Builder(this)
            .setTitle("确认删除")
            .setMessage("确定要删除这个存档吗？此操作不可撤销。")
            .setPositiveButton("删除") { _, _ ->
                launch {
                    try {
                        gameSaveDao.delete(save)
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "存档已删除", Toast.LENGTH_SHORT).show()
                            // 重新加载存档列表
                            parentDialog.dismiss()
                            loadSavedGames()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "删除存档失败: ${e.message}")
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "删除存档失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    /**
     * 继续指定的保存游戏
     */
    private fun continueGame(gameSave: GameSave) {
        try {
            // 创建意图跳转到游戏界面，带上存档信息
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("LOAD_SAVE_ID", gameSave.save_id)
                putExtra("LOAD_PLAYER_ID", gameSave.player_id)
                putExtra("LOAD_NODE_ID", gameSave.current_node_id)
                putExtra("LOAD_MAP_ID", gameSave.current_map_id)
            }
            
            startActivity(intent)
            Log.i(TAG, "继续游戏，加载存档ID: ${gameSave.save_id}")
        } catch (e: Exception) {
            Log.e(TAG, "加载存档失败: ${e.message}")
            Toast.makeText(this, "加载存档失败", Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onDestroy() {
        // 取消所有协程
        cancel()
        super.onDestroy()
    }
}
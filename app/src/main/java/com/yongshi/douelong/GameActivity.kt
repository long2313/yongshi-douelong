package com.yongshi.douelong

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yongshi.douelong.database.GameDatabase
import com.yongshi.douelong.database.GameWorldGenerator
import com.yongshi.douelong.database.dao.*
import com.yongshi.douelong.database.model.*
import com.yongshi.douelong.database.model.Player
import kotlinx.coroutines.*
import java.util.Date
import kotlin.random.Random
import kotlin.math.min
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 游戏主界面
 * 负责游戏逻辑的处理和界面交互
 */
class GameActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    // Room数据库DAO
    private lateinit var playerDao: PlayerDao
    private lateinit var mapDao: MapDao
    private lateinit var mapNodeDao: MapNodeDao
    private lateinit var nodeConnectionDao: NodeConnectionDao
    private lateinit var nodeEventDao: NodeEventDao
    private lateinit var gameSaveDao: GameSaveDao
    private lateinit var playerAchievementDao: PlayerAchievementDao
    private lateinit var achievementDao: AchievementDao
    private lateinit var monsterDao: MonsterDao
    private lateinit var spanishQuestionDao: SpanishQuestionDao
    private lateinit var monsterQuestionDao: MonsterQuestionDao
    private lateinit var battleRecordDao: BattleRecordDao
    private lateinit var battleRoundDao: BattleRoundDao
    private lateinit var fillBlankQuestionDao: FillBlankQuestionDao
    private lateinit var choiceQuestionDao: ChoiceQuestionDao
    
    private var currentPlayer: Player? = null
    private var currentMapId: Long = 1L
    private var currentNodeId: Long = 1L
    private var currentMap: com.yongshi.douelong.database.model.Map? = null
    private var currentNode: MapNode? = null
    private var availableConnections: List<NodeConnection> = emptyList()

    private val TAG = "GameActivity"

    // UI元素
    private lateinit var playerNameTextView: TextView
    private lateinit var playerHealthTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var gameLogTextView: TextView
    private lateinit var optionsContainer: LinearLayout
    private lateinit var gameScrollView: ScrollView

    // 游戏选项按钮
    private lateinit var optionNorthButton: Button
    private lateinit var optionNortheastButton: Button
    private lateinit var optionEastButton: Button
    private lateinit var optionSoutheastButton: Button
    private lateinit var optionSouthButton: Button
    private lateinit var optionSouthwestButton: Button
    private lateinit var optionWestButton: Button
    private lateinit var optionNorthwestButton: Button
    private lateinit var optionCenterButton: Button
    private lateinit var saveButton: Button
    private lateinit var returnToVillageButton: Button
    private lateinit var mapButton: Button
    private lateinit var menuButton: Button

    private lateinit var database: GameDatabase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // 获取数据库实例
        database = (application as App).database

        // 初始化数据库和DAO
        initDatabase()

        // 初始化UI元素
        initUI()

        // 为gameLogTextView添加布局监听器，确保内容变化时滚动到底部
        gameLogTextView.viewTreeObserver.addOnGlobalLayoutListener {
            scrollToBottom()
        }

        // 检查是否是从存档加载
        if (intent.hasExtra("LOAD_SAVE_ID")) {
            loadSavedGame()
        } else {
            // 游戏开始时询问玩家名称并创建角色
            showNameInputDialog()
        }
    }

    /**
     * 初始化数据库和DAO
     */
    private fun initDatabase() {
        try {
            Log.i(TAG, "初始化数据库访问对象")

            // 获取各个DAO
            playerDao = database.playerDao()
            mapDao = database.mapDao()
            mapNodeDao = database.mapNodeDao()
            nodeConnectionDao = database.nodeConnectionDao()
            nodeEventDao = database.nodeEventDao()
            gameSaveDao = database.gameSaveDao()
            playerAchievementDao = database.playerAchievementDao()
            achievementDao = database.achievementDao()
            monsterDao = database.monsterDao()
            spanishQuestionDao = database.spanishQuestionDao()
            monsterQuestionDao = database.monsterQuestionDao()
            battleRecordDao = database.battleRecordDao()
            battleRoundDao = database.battleRoundDao()
            fillBlankQuestionDao = database.fillBlankQuestionDao()
            choiceQuestionDao = database.choiceQuestionDao()

            Log.i(TAG, "数据库访问对象初始化成功")
        } catch (e: Exception) {
            Log.e(TAG, "数据库访问对象初始化失败: ${e.message}")
            Toast.makeText(this, "游戏数据初始化失败", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    /**
     * 初始化UI元素
     */
    private fun initUI() {
        playerNameTextView = findViewById(R.id.playerNameTextView)
        playerHealthTextView = findViewById(R.id.playerHealthTextView)
        locationTextView = findViewById(R.id.locationTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        gameLogTextView = findViewById(R.id.gameLogTextView)
        optionsContainer = findViewById(R.id.optionsContainer)
        gameScrollView = findViewById(R.id.gameScrollView)
        menuButton = findViewById(R.id.menuButton)

        optionNorthButton = findViewById(R.id.optionNorthButton)
        optionNortheastButton = findViewById(R.id.optionNortheastButton)
        optionEastButton = findViewById(R.id.optionEastButton)
        optionSoutheastButton = findViewById(R.id.optionSoutheastButton)
        optionSouthButton = findViewById(R.id.optionSouthButton)
        optionSouthwestButton = findViewById(R.id.optionSouthwestButton)
        optionWestButton = findViewById(R.id.optionWestButton)
        optionNorthwestButton = findViewById(R.id.optionNorthwestButton)
        optionCenterButton = findViewById(R.id.optionCenterButton)
        saveButton = findViewById(R.id.saveButton)
        returnToVillageButton = findViewById(R.id.returnToVillageButton)
        mapButton = findViewById(R.id.mapButton)

        // 设置按钮点击事件
        saveButton.setOnClickListener {
            saveGame()
        }

        returnToVillageButton.setOnClickListener {
            returnToVillage()
        }
        
        mapButton.setOnClickListener {
            showMapInfo()
        }

        menuButton.setOnClickListener {
            showGameMenu()
        }
        
        // 设置游戏日志自动滚动
        setupAutoScrollForGameLog()
    }

    /**
     * 显示输入玩家名称的对话框
     */
    private fun showNameInputDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input_name, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)

        AlertDialog.Builder(this)
            .setTitle(R.string.create_character)
            .setView(dialogView)
            .setCancelable(false)
            .setPositiveButton(R.string.dialog_confirm) { _, _ ->
                val playerName = editTextName.text.toString().trim()
                if (playerName.isNotEmpty()) {
                    createNewPlayer(playerName)
                } else {
                    createNewPlayer(getString(R.string.player_name_placeholder))
                }
            }
            .show()
    }

    /**
     * 创建新玩家角色
     */
    private fun createNewPlayer(name: String) {
        try {
            launch {
                try {
                    // 从启动时预置的地图节点里取起点（App.onCreate 已确保数据存在）
                    val startNode = withContext(Dispatchers.IO) {
                        mapNodeDao.findById(1L) ?: mapNodeDao.findAll().firstOrNull()
                    }
                    if (startNode == null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@GameActivity, "没有可用的地图节点，无法创建角色", Toast.LENGTH_LONG).show()
                            finish()
                        }
                        return@launch
                    }
                    val mapId = startNode.map_id
                    val startingNodeId = startNode.node_id
                    
                    // 创建新玩家
                    val player = Player(
                        player_id = 0,
                        name = name,
                        level = 1,
                        exp = 0,
                        max_hp = 100,
                        current_hp = 100,
                        attack = 10,
                        defense = 5,
                        gold = 0,
                        current_location = startingNodeId,
                        created_at = Date(),
                        updated_at = Date()
                    )
                    
                    // 插入玩家数据
                    val playerId = withContext(Dispatchers.IO) {
                        playerDao.insert(player)
                    }
                    
                    if (playerId > 0) {
                        // 获取完整的Player对象
                        val createdPlayer = withContext(Dispatchers.IO) {
                            playerDao.findById(playerId)
                        }
                        
                        withContext(Dispatchers.Main) {
                            currentPlayer = createdPlayer
                            currentMapId = mapId
                            currentNodeId = startingNodeId
                            updatePlayerUI()
                            
                            // 加载地图和节点数据到UI
                            loadInitialMapAndNode()
                            
                            // 初始化成就系统
//                            initAchievements()
                            
                            // 创建角色成就
//                            launch {
//                                checkAchievement("创建角色", 100, playerId)
//                            }
                            
                            Toast.makeText(this@GameActivity, "角色创建成功", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@GameActivity, "角色创建失败", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "创建角色时发生错误: ${e.message}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@GameActivity, "创建角色失败: ${e.message}", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "创建角色时发生错误: ${e.message}")
            Toast.makeText(this, "创建角色失败: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    /**
     * 加载初始地图和节点
     */
    private fun loadInitialMapAndNode() {
        try {
            launch {
                withContext(Dispatchers.IO) {
                    // 使用已生成的地图ID
                    Log.d(TAG, "尝试获取地图数据 ID: $currentMapId")
                    val map = mapDao.findById(currentMapId)
                    
                    if (map == null) {
                        withContext(Dispatchers.Main) {
                            Log.w(TAG, "没有找到地图数据")
                            Toast.makeText(this@GameActivity, "无法加载地图数据", Toast.LENGTH_LONG).show()
                        }
                        return@withContext
                    }
                    
                    // 获取地图数据
                    currentMap = map
                    Log.d(TAG, "找到地图数据: ${map.name}")

                    // 使用已生成的起始节点ID
                    Log.d(TAG, "尝试获取节点ID: $currentNodeId")
                    val node = mapNodeDao.findById(currentNodeId)
                    
                    if (node == null) {
                        withContext(Dispatchers.Main) {
                            Log.e(TAG, "找不到指定的节点")
                            Toast.makeText(this@GameActivity, "加载地图节点失败", Toast.LENGTH_LONG).show()
                        }
                        return@withContext
                    }
                    
                    Log.d(TAG, "找到节点: ${node.name}")
                    currentNode = node

                    // 加载并显示当前节点内容
                    withContext(Dispatchers.Main) {
                        loadAndDisplayNodeContent()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "加载初始地图和节点失败: ${e.message}", e)
            Toast.makeText(this, "初始化游戏世界失败", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 加载并显示当前节点内容
     */
    private fun loadAndDisplayNodeContent() {
        try {
            currentNode?.let { node ->
                // 更新UI显示当前节点信息
                locationTextView.text = node.name
                descriptionTextView.text = node.description
                
                // 检查当前节点是否是龙巢
                if (node.is_dragon_lair) {
                    handleDragonLair()
                }

                // 加载节点的事件 - 这里需要新增一个方法，暂时注释掉事件相关逻辑
                /*
                val events = nodeEventDao.findByNodeId(currentNodeId)

                // 根据事件类型执行相应的逻辑
                if (events.isNotEmpty()) {
                    // 处理第一个事件（这里可以扩展为处理多个事件）
                    processNodeEvent(events[0])
                }
                */

                // 加载可用的连接
                loadNodeConnections()
                
                // 确保滚动到底部显示最新内容
                scrollToBottom()
            }
        } catch (e: Exception) {
            Log.e(TAG, "加载节点内容时出错: ${e.message}")
            Toast.makeText(this, "加载节点内容时出错", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 处理龙穴遭遇
     */
    private fun handleDragonLair() {
        launch {
            try {
                // 延迟一下，增加紧张气氛
                delay(1000)
                
                withContext(Dispatchers.Main) {
                    logGameEvent("你站在恶龙巢穴的入口，空气中弥漫着硫磺和灰烬的气息...")
                    delay(500)
                    logGameEvent("巨大的恶龙似乎已经注意到了你的存在，它缓缓抬起头...")
                    delay(500)
                    
                    // 获取玩家等级
                    val playerLevel = currentPlayer?.level ?: 1
                    
                    // 创建龙的属性，强度基于玩家等级
                    val dragonLevel = playerLevel + 5
                    val dragonHp = dragonLevel * 50
                    val dragonAttack = dragonLevel * 8
                    val dragonDefense = dragonLevel * 5
                    
                    // 显示Boss战对话框
                    AlertDialog.Builder(this@GameActivity)
                        .setTitle("最终挑战")
                        .setMessage("你面对的是恶龙（等级：$dragonLevel）\n生命值：$dragonHp\n攻击力：$dragonAttack\n防御力：$dragonDefense\n\n你准备好挑战恶龙了吗？")
                        .setPositiveButton("战斗") { _, _ ->
                            // 开始Boss战
                            battleBoss(dragonLevel, dragonHp, dragonAttack, dragonDefense)
                        }
                        .setNegativeButton("后退") { _, _ ->
                            // 玩家决定后退
                            logGameEvent("你决定暂时撤退，等到准备充分再来挑战恶龙。")
                            returnToVillage()
                        }
                        .setCancelable(false)
                        .show()
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "处理龙穴遭遇失败: ${e.message}")
                withContext(Dispatchers.Main) {
                    logGameEvent("恶龙似乎暂时离开了巢穴，你可以稍后再来挑战。")
                }
            }
        }
    }
    
    /**
     * Boss战斗
     */
    private fun battleBoss(dragonLevel: Int, dragonHp: Int, dragonAttack: Int, dragonDefense: Int) {
        launch {
            try {
                var playerHp = currentPlayer?.current_hp ?: 100
                var bossCurrentHp = dragonHp
                var roundCount = 1
                
                // 战斗日志
                withContext(Dispatchers.Main) {
                    logGameEvent("===== 最终战斗开始 =====")
                    logGameEvent("恶龙怒吼着向你扑来！")
                }
                
                while (playerHp > 0 && bossCurrentHp > 0) {
                    val playerAttack = currentPlayer?.attack ?: 10
                    val playerDefense = currentPlayer?.defense ?: 5
                    
                    // 玩家攻击
                    val playerDamage = maxOf(1, playerAttack - dragonDefense / 2)
                    bossCurrentHp -= playerDamage
                    
                    withContext(Dispatchers.Main) {
                        logGameEvent("回合 $roundCount: 你对恶龙造成了 $playerDamage 点伤害。")
                    }
                    
                    // 检查Boss是否死亡
                    if (bossCurrentHp <= 0) {
                        bossBattleVictory()
                        break
                    }
                    
                    // Boss攻击
                    // Boss有20%概率进行特殊攻击
                    if (Random.nextDouble() < 0.2) {
                        val specialDamage = dragonAttack * 2 - playerDefense
                        val actualDamage = maxOf(5, specialDamage)
                        playerHp -= actualDamage
                        
                        withContext(Dispatchers.Main) {
                            logGameEvent("回合 $roundCount: 恶龙使用了火焰吐息！对你造成了 $actualDamage 点伤害！")
                        }
                    } else {
                        val dragonDamage = maxOf(1, dragonAttack - playerDefense / 2)
                        playerHp -= dragonDamage
                        
                        withContext(Dispatchers.Main) {
                            logGameEvent("回合 $roundCount: 恶龙对你造成了 $dragonDamage 点伤害。")
                        }
                    }
                    
                    // 更新玩家生命值
                    currentPlayer?.let { player ->
                        withContext(Dispatchers.IO) {
                            player.current_hp = playerHp
                            playerDao.update(player)
                        }
                        
                        withContext(Dispatchers.Main) {
                            updatePlayerUI()
                        }
                    }
                    
                    roundCount++
                    
                    // 为了动画效果，添加短暂延迟
                    delay(1000)
                    
                    // 检查玩家是否死亡
                    if (playerHp <= 0) {
                        bossBattleDefeat()
                        break
                    }
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Boss战斗失败: ${e.message}")
                withContext(Dispatchers.Main) {
                    logGameEvent("战斗过程中出现了意外，你暂时逃离了龙穴...")
                    returnToVillage()
                }
            }
        }
    }
    
    /**
     * Boss战胜利
     */
    private fun bossBattleVictory() {
        launch {
            try {
                withContext(Dispatchers.Main) {
                    logGameEvent("恶龙发出一声凄厉的吼叫，轰然倒地！")
                    logGameEvent("你成功击败了恶龙，解救了翡翠森林大陆！")
                    logGameEvent("===== 最终战斗结束 =====")
                }
                
                // 计算奖励 - 经验和金币，Boss奖励丰厚
                val expGained = 500
                val goldGained = 1000
                
                // 更新玩家数据
                currentPlayer?.let { player ->
                    withContext(Dispatchers.IO) {
                        player.exp += expGained
                        player.gold += goldGained
                        
                        // 检查是否升级
                        checkLevelUp(player)
                        
                        playerDao.update(player)
                        
                        // 更新成就 - 击败龙
                        checkAchievement("击败恶龙", 100, player.player_id)
                    }
                    
                    withContext(Dispatchers.Main) {
                        logGameEvent("你获得了 $expGained 点经验和 $goldGained 金币！")
                        updatePlayerUI()
                        
                        // 显示游戏胜利对话框
                        AlertDialog.Builder(this@GameActivity)
                            .setTitle("恭喜！")
                            .setMessage("你成功击败了恶龙，拯救了整个大陆！\n\n你获得了:\n- $expGained 点经验\n- $goldGained 金币\n\n恭喜你通关游戏！")
                            .setPositiveButton("继续探索") { _, _ ->
                                returnToVillage()
                            }
                            .setCancelable(false)
                            .show()
                    }
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "处理Boss战胜利失败: ${e.message}")
                withContext(Dispatchers.Main) {
                    logGameEvent("你击败了恶龙，但是在收集战利品时遇到了一些问题...")
                    returnToVillage()
                }
            }
        }
    }
    
    /**
     * Boss战失败
     */
    private fun bossBattleDefeat() {
        launch {
            try {
                withContext(Dispatchers.Main) {
                    logGameEvent("你被恶龙击倒在地，失去了意识...")
                    logGameEvent("===== 最终战斗结束 =====")
                    delay(1000)
                    logGameEvent("......")
                    delay(300)
                    logGameEvent("你醒来时，发现自己躺在村庄的医疗室里。")
                    logGameEvent("村长告诉你，是一队巡逻的冒险者发现了你，并将你带回了村庄。")
                    
                    // 恢复生命值
                    currentPlayer?.let { player ->
                        withContext(Dispatchers.IO) {
                            player.current_hp = player.max_hp / 2
                            playerDao.update(player)
                        }
                        
                        withContext(Dispatchers.Main) {
                            updatePlayerUI()
                            
                            AlertDialog.Builder(this@GameActivity)
                                .setTitle("战斗失败")
                                .setMessage("你被恶龙击败了。幸运的是，你被一队巡逻的冒险者救了回来。\n\n你需要变得更强大才能击败恶龙！")
                                .setPositiveButton("继续冒险") { _, _ ->
                                    // 返回村庄
                                    returnToVillage()
                                }
                                .setCancelable(false)
                                .show()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "处理Boss战失败: ${e.message}")
                withContext(Dispatchers.Main) {
                    logGameEvent("你在战斗中失败，醒来时发现自己回到了村庄...")
                    returnToVillage()
                }
            }
        }
    }

    /**
     * 更新玩家信息UI
     */
    private fun updatePlayerUI() {
        currentPlayer?.let { player ->
            playerNameTextView.text = "${player.name} Lv.${player.level}"
            playerHealthTextView.text = "HP: ${player.current_hp}/${player.max_hp}"
            
            // 计算当前等级所需经验值
            val expNeeded = (100 * Math.pow(1.3, player.level.toDouble() - 1)).toInt()
            // 添加经验值显示
            val expTextView = findViewById<TextView>(R.id.playerExpTextView)
            expTextView.text = "EXP: ${player.exp}/$expNeeded"
        }
    }

    /**
     * 保存游戏状态
     */
    private fun saveGame() {
        currentPlayer?.let { player ->
            try {
                // 创建存档名称
                val currentNodeName = currentNode?.name ?: "未知位置"
                val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                val currentTime = Date()
                val saveName = "${player.name} - $currentNodeName - ${dateFormat.format(currentTime)}"

                // 创建玩家状态字符串
                val playerState = "HP:${player.current_hp}/${player.max_hp},EXP:${player.exp},GOLD:${player.gold}"

                // 创建GameSave对象
                val gameSave = GameSave(
                    save_id = 0, // Room会自动生成ID
                    player_id = player.player_id,
                    save_name = saveName,
                    save_time = currentTime,
                    current_node_id = currentNodeId,
                    current_map_id = currentMapId,
                    player_state = playerState,
                    is_auto_save = false
                )

                // 在协程中执行数据库操作
                launch {
                    val saveId = withContext(Dispatchers.IO) {
                        gameSaveDao.insert(gameSave)
                    }

                    if (saveId > 0) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@GameActivity, "游戏保存成功", Toast.LENGTH_SHORT).show()
                            logGameEvent("游戏已保存")
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@GameActivity, "游戏保存失败", Toast.LENGTH_LONG).show()
                            Log.e(TAG, "游戏保存失败")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "游戏保存失败: ${e.message}")
                Toast.makeText(this, "游戏保存失败: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } ?: run {
            Toast.makeText(this, "没有角色数据，无法保存", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 返回村庄方法
     */
    private fun returnToVillage() {
        launch {
            try {
                // 寻找村庄或起始节点
                val startNode = withContext(Dispatchers.IO) {
                    mapNodeDao.findStartingNode(currentMapId)
                }
                
                if (startNode != null) {
                    // 更新玩家位置
                    currentPlayer?.let { player ->
                        withContext(Dispatchers.IO) {
                            player.current_location = startNode.node_id
                            playerDao.update(player)
                        }
                    }
                    
                    // 更新当前节点
                    currentNodeId = startNode.node_id
                    currentNode = startNode
                    
                    withContext(Dispatchers.Main) {
                        logGameEvent("你回到了村庄。")
                        loadAndDisplayNodeContent()
                    }
                } else {
                    Log.e(TAG, "无法找到起始节点")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@GameActivity, "无法返回村庄", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "返回村庄失败: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GameActivity, "返回村庄失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 加载保存的游戏
     */
    private fun loadSavedGame() {
        try {
            val saveId = intent.getLongExtra("LOAD_SAVE_ID", -1)
            val playerId = intent.getLongExtra("LOAD_PLAYER_ID", -1)

            if (saveId <= 0 || playerId <= 0) {
                Toast.makeText(this, "无效的存档数据", Toast.LENGTH_LONG).show()
                finish()
                return
            }

            // 在协程中执行数据库操作
            launch {
                // 加载玩家数据
                val player = withContext(Dispatchers.IO) {
                    playerDao.findById(playerId)
                }

                if (player == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@GameActivity, "无法加载玩家数据", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    return@launch
                }

                // 加载存档数据
                val gameSave = withContext(Dispatchers.IO) {
                    gameSaveDao.findById(saveId)
                }

                if (gameSave == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@GameActivity, "无法加载存档数据", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    return@launch
                }

                // 设置当前玩家和位置
                currentPlayer = player
                currentNodeId = gameSave.current_node_id
                currentMapId = gameSave.current_map_id

                // 更新UI
                updatePlayerUI()

                // 加载地图和节点
                loadMapAndNode(currentMapId, currentNodeId)
                
                // 初始化成就系统
//                initAchievements()

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GameActivity, "游戏加载成功", Toast.LENGTH_SHORT).show()
                    logGameEvent("加载了存档: ${gameSave.save_name}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "加载存档失败: ${e.message}")
            Toast.makeText(this, "加载存档失败: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    /**
     * 显示库存
     * 暂未实现
     */
    private fun showInventory() {
        Toast.makeText(this, "背包功能尚未实现", Toast.LENGTH_SHORT).show()
    }

    /**
     * 显示游戏菜单
     */
    private fun showGameMenu() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_game_menu, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("游戏菜单")
            .setView(dialogView)
            .create()

        // 获取视图引用
        val contentText = dialogView.findViewById<TextView>(R.id.contentText)
        
        // 设置按钮点击事件
        dialogView.findViewById<Button>(R.id.playerInfoBtn).setOnClickListener {
            showPlayerInfoContent(contentText)
        }
        
        dialogView.findViewById<Button>(R.id.inventoryBtn).setOnClickListener {
            showInventoryContent(contentText)
        }
        
        dialogView.findViewById<Button>(R.id.savesBtn).setOnClickListener {
            showSavesContent(contentText)
        }
        
        dialogView.findViewById<Button>(R.id.achievementsBtn).setOnClickListener {
            showAchievementsContent(contentText)
        }
        
        dialogView.findViewById<Button>(R.id.settingsBtn).setOnClickListener {
            showSettingsContent(contentText)
        }
        
        dialogView.findViewById<Button>(R.id.exitBtn).setOnClickListener {
            dialog.dismiss()
            showExitConfirmationDialog()
        }

        // 默认显示玩家信息
        showPlayerInfoContent(contentText)
        
        dialog.show()
    }

    /**
     * 显示玩家信息内容
     */
    private fun showPlayerInfoContent(contentText: TextView) {
        currentPlayer?.let { player ->
            val content = """
                玩家名称：${player.name}
                等级：${player.level}
                经验值：${player.exp}
                
                生命值：${player.current_hp}/${player.max_hp}
                攻击力：${player.attack}
                防御力：${player.defense}
                
                金币：${player.gold}
                
                创建时间：${player.created_at}
                游戏时长：${calculatePlayTime(player.created_at)}
            """.trimIndent()
            
            contentText.text = content
        }
    }

    /**
     * 显示背包内容
     */
    private fun showInventoryContent(contentText: TextView) {
        contentText.text = "背包功能正在开发中...\n\n这里将显示你的所有物品。"
    }

    /**
     * 显示存档列表内容
     */
    private fun showSavesContent(contentText: TextView) {
        launch {
            try {
                val saves = withContext(Dispatchers.IO) {
                    currentPlayer?.let { player ->
                        gameSaveDao.findByPlayerId(player.player_id)
                    } ?: emptyList()
                }
                
                val content = if (saves.isEmpty()) {
                    "暂无存档记录"
                } else {
                    val saveList = saves.joinToString("\n\n") { save ->
                        """
                        存档名称：${save.save_name}
                        保存时间：${save.save_time}
                        ${if (save.is_auto_save) "[自动存档]" else "[手动存档]"}
                        """.trimIndent()
                    }
                    saveList
                }
                
                withContext(Dispatchers.Main) {
                    contentText.text = content
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    contentText.text = "加载存档列表失败：${e.message}"
                }
            }
        }
    }

    /**
     * 显示成就列表内容
     */
    private fun showAchievementsContent(contentText: TextView) {
        launch {
            try {
                val achievements = withContext(Dispatchers.IO) {
                    currentPlayer?.let { player ->
                        playerAchievementDao.getPlayerAchievementDetails(player.player_id)
                    } ?: emptyList()
                }
                
                val content = if (achievements.isEmpty()) {
                    "暂无成就记录"
                } else {
                    val achievementList = achievements.joinToString("\n\n") { achievement ->
                        """
                        ${achievement.name}
                        进度：${achievement.progress}%
                        ${if (achievement.progress >= 100) if (achievement.is_claimed) "✓ 已完成" else "☑ 已解锁（未领取）" else ""}
                        ${achievement.description}
                        """.trimIndent()
                    }
                    achievementList
                }
                
                withContext(Dispatchers.Main) {
                    contentText.text = content
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    contentText.text = "加载成就列表失败：${e.message}"
                }
            }
        }
    }

    /**
     * 显示设置内容
     */
    private fun showSettingsContent(contentText: TextView) {
        contentText.text = "设置功能正在开发中...\n\n这里将提供游戏相关设置选项。"
    }

    /**
     * 计算游戏时长
     */
    private fun calculatePlayTime(startTime: Date): String {
        val playTimeMillis = System.currentTimeMillis() - startTime.time
        val hours = playTimeMillis / (1000 * 60 * 60)
        val minutes = (playTimeMillis % (1000 * 60 * 60)) / (1000 * 60)
        return "${hours}小时${minutes}分钟"
    }

    /**
     * 显示退出确认对话框
     */
    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("返回主菜单")
            .setMessage("确定要返回主菜单吗？未保存的游戏进度将会丢失。")
            .setPositiveButton("确定") { _, _ ->
                finish()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    /**
     * 加载地图和节点
     */
    private fun loadMapAndNode(mapId: Long, nodeId: Long) {
        launch {
            try {
                // 读取地图数据
                val map = withContext(Dispatchers.IO) {
                    mapDao.findById(mapId)
                }

                if (map == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@GameActivity, "无法加载地图数据", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                // 读取节点数据
                val node = withContext(Dispatchers.IO) {
                    mapNodeDao.findById(nodeId)
                }

                if (node == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@GameActivity, "无法加载节点数据", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                // 获取可用连接 - 暂时置空
                val connections = withContext(Dispatchers.IO) {
                    emptyList<NodeConnection>() // 临时使用空列表
                    // nodeConnectionDao.findConnectionsFromNode(nodeId)
                }

                // 更新当前状态
                currentMap = map
                currentNode = node
                availableConnections = connections

                // 更新UI
                withContext(Dispatchers.Main) {
                    updateLocationUI()
                    updateConnectionOptions()
                }

            } catch (e: Exception) {
                Log.e(TAG, "加载地图和节点失败: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GameActivity, "加载地图和节点失败: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * 在Activity销毁时执行清理
     */
    override fun onDestroy() {
        // 取消所有协程
        cancel()
        super.onDestroy()
    }

    /**
     * 在游戏日志中添加事件记录
     */
    private fun logGameEvent(message: String) {
        gameLogTextView.append("$message\n")
        // 添加内容后滚动到底部
        scrollToBottom()
    }

    /**
     * 将ScrollView滚动到底部
     */
    private fun scrollToBottom() {
        try {
            // 使用post确保在UI渲染后执行滚动
            gameScrollView.post {
                // 强制重新布局以确保所有内容都已测量
                gameScrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
        } catch (e: Exception) {
            Log.e(TAG, "滚动到底部失败: ${e.message}")
        }
    }

    /**
     * 创建自定义TextWatcher来监听文本变化并自动滚动
     */
    private fun setupAutoScrollForGameLog() {
        gameLogTextView.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: android.text.Editable?) {
                scrollToBottom()
            }
        })
    }
    
    /**
     * 更新位置UI
     */
    private fun updateLocationUI() {
        currentNode?.let { node ->
            locationTextView.text = node.name
            descriptionTextView.text = node.description
            logGameEvent("你来到了${node.name}")
            // 自动滚动到底部
            scrollToBottom()
        }
    }

    /**
     * 更新连接选项
     */
    private fun updateConnectionOptions() {
        loadNodeConnections()
    }

    /**
     * 加载节点连接
     */
    private fun loadNodeConnections() {
        try {
            launch {
                availableConnections = withContext(Dispatchers.IO) {
                    nodeConnectionDao.findConnectionsFromNode(currentNodeId)
                }
                
                withContext(Dispatchers.Main) {
                    // 隐藏所有选项按钮
                    hideAllOptionButtons()
                    
                    // 显示探索按钮
                    optionCenterButton.apply {
                        text = "探索周围"
                        visibility = View.VISIBLE
                        setOnClickListener {
                            exploreArea()
                        }
                    }
                    
                    // 显示可用的连接选项
                    displayConnectionOptions()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "加载节点连接失败: ${e.message}")
            Toast.makeText(this, "加载路径选择失败", Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * 隐藏所有选项按钮
     */
    private fun hideAllOptionButtons() {
        optionNorthButton.visibility = View.GONE
        optionNortheastButton.visibility = View.GONE
        optionEastButton.visibility = View.GONE
        optionSoutheastButton.visibility = View.GONE
        optionSouthButton.visibility = View.GONE
        optionSouthwestButton.visibility = View.GONE
        optionWestButton.visibility = View.GONE
        optionNorthwestButton.visibility = View.GONE
        optionCenterButton.visibility = View.GONE
    }
    
    /**
     * 显示连接选项
     */
    private fun displayConnectionOptions() {
        val directionButtons = mapOf(
            "北" to optionNorthButton,
            "东北" to optionNortheastButton,
            "东" to optionEastButton,
            "东南" to optionSoutheastButton,
            "南" to optionSouthButton,
            "西南" to optionSouthwestButton,
            "西" to optionWestButton,
            "西北" to optionNorthwestButton
        )
        
        launch {
            for (connection in availableConnections) {
                val direction = connection.direction
                val button = direction?.let { directionButtons[it] }
                
                if (button != null) {
                    // 获取目标节点信息
                    val targetNode = withContext(Dispatchers.IO) {
                        mapNodeDao.findById(connection.to_node_id)
                    }
                    
                    withContext(Dispatchers.Main) {
                        button.apply {
                            text = if (connection.is_locked) "${direction} 🔒" else direction
                            visibility = View.VISIBLE
                            setOnClickListener {
                                if (connection.is_locked) {
                                    handleLockedPath(connection)
                                } else {
                                    moveToNode(connection.to_node_id)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 处理锁定的路径
     */
    private fun handleLockedPath(connection: NodeConnection) {
        // 检查解锁条件
        val unlockCondition = connection.unlock_condition ?: "需要击败守卫才能通过"
        
        AlertDialog.Builder(this)
            .setTitle("路径锁定")
            .setMessage("这条路径已被锁定。$unlockCondition")
            .setPositiveButton("尝试解锁") { _, _ ->
                // 这里可以实现解锁逻辑，例如战斗、解谜等
                // 暂时直接解锁
                launch {
                    try {
                        withContext(Dispatchers.IO) {
                            connection.is_locked = false
                            nodeConnectionDao.update(connection)
                        }
                        
                        withContext(Dispatchers.Main) {
                            logGameEvent("你成功解锁了通往${connection.direction}的路径！")
                            loadNodeConnections()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "解锁路径失败: ${e.message}")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@GameActivity, "解锁失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("返回", null)
            .show()
    }
    
    /**
     * 移动到指定节点
     */
    private fun moveToNode(nodeId: Long) {
        try {
            // 获取目标节点
            launch {
                val targetNode = withContext(Dispatchers.IO) {
                    mapNodeDao.findById(nodeId)
                }
                
                if (targetNode != null) {
                    // 更新当前节点
                    currentNodeId = nodeId
                    currentNode = targetNode
                    
                    // 更新玩家位置
                    currentPlayer?.let { player ->
                        withContext(Dispatchers.IO) {
                            player.current_location = nodeId
                            playerDao.update(player)
                        }
                    }
                    
                    // 加载并显示节点内容
                    withContext(Dispatchers.Main) {
                        loadAndDisplayNodeContent()
                    }
                } else {
                    Log.e(TAG, "无法找到目标节点: $nodeId")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@GameActivity, "无法前往目标位置", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "移动到节点失败: ${e.message}")
            Toast.makeText(this, "移动失败", Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * 检查是否升级
     */
    private fun checkLevelUp(player: Player) {
        // 使用更合理的经验计算公式：基础100经验，每级递增30%
        val baseExp = 100
        val expNeeded = (baseExp * Math.pow(1.3, player.level.toDouble() - 1)).toInt()
        
        if (player.exp >= expNeeded) {
            // 扣除升级所需经验值，保留剩余经验
            player.exp -= expNeeded
            player.level++
            
            // 属性提升也采用非线性增长
            val hpIncrease = 15 + (player.level / 5) * 5  // 基础+15，每5级额外+5
            val attackIncrease = 3 + (player.level / 10) * 2  // 基础+3，每10级额外+2
            val defenseIncrease = 2 + (player.level / 10)  // 基础+2，每10级额外+1
            
            player.max_hp += hpIncrease
            player.current_hp = player.max_hp  // 升级时恢复满血
            player.attack += attackIncrease
            player.defense += defenseIncrease
            
            // 在主线程通知玩家升级
            MainScope().launch {
                logGameEvent("恭喜！你升级了！现在是 ${player.level} 级。")
                logGameEvent("生命值提升：+$hpIncrease (当前 ${player.max_hp})")
                logGameEvent("攻击力提升：+$attackIncrease (当前 ${player.attack})")
                logGameEvent("防御力提升：+$defenseIncrease (当前 ${player.defense})")
                logGameEvent("下一级需要 ${calculateNextLevelExp(player.level)} 经验")
                
                Toast.makeText(this@GameActivity, "恭喜！你升级了！", Toast.LENGTH_LONG).show()
            }
            
            // 更新升级相关成就
            launch {
                checkAchievement("达到等级5", if (player.level >= 5) 100 else player.level * 20, player.player_id)
                checkAchievement("达到等级10", if (player.level >= 10) 100 else player.level * 10, player.player_id)
                checkAchievement("达到等级20", if (player.level >= 20) 100 else player.level * 5, player.player_id)
            }
            
            // 如果还有足够经验继续升级，递归检查
            if (player.exp >= calculateNextLevelExp(player.level)) {
                checkLevelUp(player)
            }
        }
    }
    
    /**
     * 计算下一级所需经验值
     */
    private fun calculateNextLevelExp(level: Int): Int {
        val baseExp = 100
        return (baseExp * Math.pow(1.3, level.toDouble() - 1)).toInt()
    }

    /**
     * 计算战斗胜利获得的经验值
     */
    private fun calculateBattleExp(monster: Monster, playerLevel: Int): Int {
        // 基础经验值：怪物等级 * 15
        var expReward = monster.level * 15
        
        // 根据怪物难度增加经验值
        val difficultyMultiplier = when (monster.difficulty) {
            "EASY" -> 0.8
            "NORMAL" -> 1.0
            "HARD" -> 1.3
            "ELITE" -> 1.8
            else -> 1.0
        }
        expReward = (expReward * difficultyMultiplier).toInt()
        
        // 根据等级差异调整经验值
        val levelDiff = monster.level - playerLevel
        val levelMultiplier = when {
            levelDiff >= 5 -> 1.5  // 击败高于自己5级以上的怪物，获得50%额外经验
            levelDiff >= 3 -> 1.3  // 击败高于自己3-4级的怪物，获得30%额外经验
            levelDiff >= 1 -> 1.1  // 击败高于自己1-2级的怪物，获得10%额外经验
            levelDiff <= -5 -> 0.3 // 击败低于自己5级以上的怪物，经验减少70%
            levelDiff <= -3 -> 0.5 // 击败低于自己3-4级的怪物，经验减少50%
            levelDiff <= -1 -> 0.8 // 击败低于自己1-2级的怪物，经验减少20%
            else -> 1.0           // 同等级怪物，正常经验
        }
        expReward = (expReward * levelMultiplier).toInt()
        
        // 添加随机波动（±15%）
        val variation = expReward * 0.15
        expReward += Random.nextInt((-variation).toInt(), variation.toInt() + 1)
        
        // 确保最少获得1点经验
        return maxOf(1, expReward)
    }
    
    /**
     * 处理战斗胜利
     */
    private fun handleBattleVictory(monster: Monster) {
        launch {
            try {
                currentPlayer?.let { player ->
                    // 计算经验和金币奖励
                    val expGained = calculateBattleExp(monster, player.level)
                    val goldGained = calculateBattleGold(monster, player.level)
                    
                    withContext(Dispatchers.IO) {
                        player.exp += expGained
                        player.gold += goldGained
                        
                        // 检查是否升级
                        checkLevelUp(player)
                        
                        playerDao.update(player)
                    }
                    
                    withContext(Dispatchers.Main) {
                        logGameEvent("你赢得了战斗！")
                        logGameEvent("获得 $expGained 点经验")
                        logGameEvent("获得 $goldGained 金币")
                        updatePlayerUI()
                    }
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "处理战斗奖励时出错: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GameActivity, "获取奖励失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    /**
     * 计算战斗胜利获得的金币
     */
    private fun calculateBattleGold(monster: Monster, playerLevel: Int): Int {
        // 基础金币：怪物等级 * 8
        var goldReward = monster.level * 8
        
        // 根据怪物难度增加金币
        val difficultyMultiplier = when (monster.difficulty) {
            "EASY" -> 0.8
            "NORMAL" -> 1.0
            "HARD" -> 1.4
            "ELITE" -> 2.0
            else -> 1.0
        }
        goldReward = (goldReward * difficultyMultiplier).toInt()
        
        // 添加随机波动（±20%）
        val variation = goldReward * 0.2
        goldReward += Random.nextInt((-variation).toInt(), variation.toInt() + 1)
        
        // 确保最少获得1金币
        return maxOf(1, goldReward)
    }
    
    /**
     * 遇到敌人
     */
    private fun encounterEnemy() {
        try {
            val monsterCount = monsterDao.getMonsterCount()
            
            // 获取或生成怪物
            val monster = if (monsterCount >= 100) {
                // 从数据库中随机抽取一只怪物
                val difficulties = listOf("EASY", "NORMAL", "HARD", "ELITE")
                val randomDifficulty = difficulties.random()
                monsterDao.getRandomMonsterByDifficulty(randomDifficulty)
            } else {
                // 根据当前区域和玩家等级生成新怪物
                val enemyTypes = listOf("哥布林", "狼", "强盗", "骷髅", "蜘蛛")
                val enemyName = enemyTypes.random()
                
                // 计算敌人属性（基于玩家等级）
                val playerLevel = currentPlayer?.level ?: 1
                // 敌人等级浮动范围随玩家等级提升
                val levelVariation = (playerLevel / 5) + 1
                val enemyLevel = playerLevel + Random.nextInt(-levelVariation, levelVariation + 1)
                
                // 生命值采用指数增长，并加入更多随机性
                val baseHp = 15 + (enemyLevel * enemyLevel * 2.5).toInt()
                val hpVariation = (baseHp * 0.2).toInt()
                val enemyHp = baseHp + Random.nextInt(-hpVariation, hpVariation + 1)
                
                // 攻击力与等级的关系更加显著，并提供更大的随机范围
                val baseAttack = 5 + (enemyLevel * 2.5).toInt()
                val attackVariation = (baseAttack * 0.3).toInt()
                val enemyAttack = baseAttack + Random.nextInt(-attackVariation, attackVariation + 1)
                
                // 防御力采用开方增长，确保其效果随等级提升而显著
                val baseDefense = 2 + kotlin.math.sqrt(enemyLevel.toDouble() * 3).toInt()
                val defenseVariation = (baseDefense * 0.2).toInt()
                val enemyDefense = baseDefense + Random.nextInt(-defenseVariation, defenseVariation + 1)
                
                // 根据等级决定难度
                val difficulty = when {
                    enemyLevel > playerLevel + 1 -> "ELITE"
                    enemyLevel > playerLevel -> "HARD"
                    enemyLevel == playerLevel -> "NORMAL"
                    else -> "EASY"
                }
                
                // 创建新的怪物记录
                val newMonster = Monster(
                    name = enemyName,
                    description = "一只${enemyLevel}级的${enemyName}",
                    level = enemyLevel,
                    hp = enemyHp,
                    attack = enemyAttack,
                    defense = enemyDefense,
                    exp_reward = enemyLevel * 10 + Random.nextInt(10, 15),
                    gold_reward = enemyLevel * 5,
                    difficulty = difficulty
                )
                
                // 插入怪物记录并返回带ID的怪物对象
                val monsterId = monsterDao.insert(newMonster)

                // 获取对应难度的题目
                val questionDifficulty = when (newMonster.difficulty) {
                    "EASY" -> 1..1
                    "NORMAL" -> 2..3
                    "HARD" -> 2..4
                    "ELITE" -> 3..4
                    else -> 4..5
                }

                // 随机选择5道对应难度的题目
                val questions = spanishQuestionDao.getRandomQuestionsByDifficultyRange(
                    minDifficulty = questionDifficulty.first,
                    maxDifficulty = questionDifficulty.last,
                    limit = 5
                )

                // 创建怪物-题目关联
                questions.forEach { question ->
                    val monsterQuestion = MonsterQuestion(
                        monster_id = monsterId,
                        question_id = question.question_id
                    )
                    monsterQuestionDao.insert(monsterQuestion)
                }
                newMonster.copy(monster_id = monsterId)

            }

            logGameEvent("你遇到了一只${monster.level}级的${monster.name}！")
            
            // 显示战斗对话框
            AlertDialog.Builder(this)
                .setTitle("遭遇战斗")
                .setMessage("你遇到了一只${monster.level}级的${monster.name}\n生命值：${monster.hp}\n攻击力：${monster.attack}\n防御力：${monster.defense}")
                .setPositiveButton("战斗") { _, _ ->
                    // 开始战斗
                    battle(monster)
                }
                .setNegativeButton("逃跑") { _, _ ->
                    // 尝试逃跑（70%成功率）
                    if (Random.nextInt(100) < 70) {
                        logGameEvent("你成功地从${monster.name}面前逃脱了。")
                    } else {
                        logGameEvent("你试图逃跑，但${monster.name}拦住了你的去路！")
                        // 逃跑失败，强制战斗，并且敌人先攻
                        battle(monster, true)
                    }
                }
                .setCancelable(false)
                .show()
        } catch (e: Exception) {
            Log.e(TAG, "生成敌人失败: ${e.message}")
            logGameEvent("你似乎感觉到了危险的气息，但什么也没有出现。")
        }
    }
    
    /**
     * 战斗
     */
    private fun battle(monster: Monster, enemyFirst: Boolean = false) {
        launch {
            try {
                var currentEnemyHp = monster.hp
                var currentPlayerHp = currentPlayer?.current_hp ?: return@launch
                var roundCount = 1
                
                // 创建战斗记录
                val battleRecord = BattleRecord(
                    player_id = currentPlayer?.player_id ?: return@launch,
                    monster_id = monster.monster_id,
                    node_id = currentNodeId,
                    map_id = currentMapId,
                    start_time = Date()
                )
                
                // 保存战斗记录并获取battle_id
                val battleId = withContext(Dispatchers.IO) {
                    battleRecordDao.insert(battleRecord)
                }
                
                withContext(Dispatchers.Main) {
                    logGameEvent("===== 战斗开始 =====")
                    logGameEvent("你遇到了 ${monster.name} (等级 ${monster.level})")
                }
                
                // 如果敌人先攻
                if (enemyFirst) {
                    val damage = maxOf(1, monster.attack - (currentPlayer?.defense ?: 0))
                    currentPlayerHp -= damage
                    withContext(Dispatchers.Main) {
                        logGameEvent("${monster.name} 偷袭了你，造成了 $damage 点伤害！")
                    }
                }
                
                // 获取怪物关联的题目
                val monsterQuestions = withContext(Dispatchers.IO) {
                    monsterQuestionDao.getSpanishQuestionsForMonster(monster.monster_id)
                }
                
                if (monsterQuestions.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        logGameEvent("错误：没有找到关联的题目，战斗中止！")
                    }
                    return@launch
                }
                
                // 战斗循环
                for (questionData in monsterQuestions) {
                    if (currentPlayerHp <= 0 || currentEnemyHp <= 0) break
                    
                    withContext(Dispatchers.Main) {
                        logGameEvent("\n----- 回合 $roundCount -----")
                        logGameEvent("你的生命值：$currentPlayerHp")
                        logGameEvent("${monster.name}的生命值：$currentEnemyHp")
                    }
                    
                    // 显示问题对话框
                    val playerAnswer = withContext(Dispatchers.Main) {
                        showQuestionDialog(questionData)
                    }
                    
                    // 创建回合记录
                    val battleRound = BattleRound(
                        battle_id = battleId,
                        round_number = roundCount,
                        question_id = questionData.question_id,
                        player_answer = playerAnswer
                    )
                    
                    var damageDealt = 0
                    var damageReceived = 0
                    
                    // 判断答案是否正确并处理伤害
                    if (isAnswerCorrect(playerAnswer, questionData)) {
                        // 回答正确，玩家攻击
                        damageDealt = maxOf(1, (currentPlayer?.attack ?: 0) - monster.defense)
                        currentEnemyHp -= damageDealt
                        withContext(Dispatchers.Main) {
                            logGameEvent("回答正确！你对 ${monster.name} 造成了 $damageDealt 点伤害！")
                        }
                        battleRound.is_correct = true
                    } else {
                        // 回答错误，敌人攻击
                        damageReceived = maxOf(1, monster.attack - (currentPlayer?.defense ?: 0))
                        currentPlayerHp -= damageReceived
                        withContext(Dispatchers.Main) {
                            logGameEvent("回答错误！")
                            logGameEvent("${monster.name} 对你造成了 $damageReceived 点伤害！")
                        }
                        battleRound.is_correct = false
                    }
                    
                    // 更新回合记录
                    battleRound.damage_dealt = damageDealt
                    battleRound.damage_received = damageReceived
                    battleRound.player_hp_after = currentPlayerHp
                    battleRound.monster_hp_after = currentEnemyHp
                    
                    // 保存回合记录
                    withContext(Dispatchers.IO) {
                        battleRoundDao.insert(battleRound)
                    }
                    
                    // 更新玩家生命值
                    currentPlayer?.let { player ->
                        withContext(Dispatchers.IO) {
                            player.current_hp = currentPlayerHp
                            playerDao.update(player)
                        }
                        withContext(Dispatchers.Main) {
                            updatePlayerUI()
                        }
                    }
                    
                    roundCount++
                    delay(1000) // 延迟一秒，让玩家有时间阅读战斗信息
                }

                // 如果战斗还未分出胜负，继续抽取题目
                while (currentPlayerHp > 0 && currentEnemyHp > 0) {
                    withContext(Dispatchers.Main) {
                        logGameEvent("\n----- 额外回合 $roundCount -----")
                        logGameEvent("你的生命值：$currentPlayerHp")
                        logGameEvent("${monster.name}的生命值：$currentEnemyHp")
                    }

                    // 获取已使用的题目ID列表
                    val usedQuestionIds = withContext(Dispatchers.IO) {
                        monsterQuestionDao.getSpanishQuestionsForMonster(monster.monster_id)
                            .map { it.question_id }
                            .toSet()
                    }

                    // 根据怪物难度获取新的随机题目，排除已使用的题目
                    val difficultyRange = when (monster.difficulty) {
                        "EASY" -> 1..1
                        "NORMAL" -> 2..3
                        "HARD" -> 2..4
                        "ELITE" -> 3..4
                        else -> 4..5
                    }

                    // 获取一道新的随机题目，排除已使用的题目
                    val newQuestion = withContext(Dispatchers.IO) {
                        spanishQuestionDao.getRandomQuestionsByDifficultyRangeExcluding(
                            minDifficulty = difficultyRange.first,
                            maxDifficulty = difficultyRange.last,
                            excludeIds = usedQuestionIds.toList(),
                            limit = 1
                        ).firstOrNull()
                    } ?: break // 如果没有获取到题目，结束战斗

                    // 将新题目添加到已使用题目列表中
                    withContext(Dispatchers.IO) {
                        val monsterQuestion = MonsterQuestion(
                            monster_id = monster.monster_id,
                            question_id = newQuestion.question_id
                        )
                        monsterQuestionDao.insert(monsterQuestion)
                    }

                    // 显示问题对话框
                    val playerAnswer = withContext(Dispatchers.Main) {
                        showQuestionDialog(newQuestion)
                    }

                    // 创建回合记录
                    val battleRound = BattleRound(
                        battle_id = battleId,
                        round_number = roundCount,
                        question_id = newQuestion.question_id,
                        player_answer = playerAnswer
                    )

                    var damageDealt = 0
                    var damageReceived = 0

                    // 判断答案是否正确并处理伤害
                    if (isAnswerCorrect(playerAnswer, newQuestion)) {
                        // 回答正确，玩家攻击
                        damageDealt = maxOf(1, (currentPlayer?.attack ?: 0) - monster.defense)
                        currentEnemyHp -= damageDealt
                        withContext(Dispatchers.Main) {
                            logGameEvent("回答正确！你对 ${monster.name} 造成了 $damageDealt 点伤害！")
                        }
                        battleRound.is_correct = true
                    } else {
                        // 回答错误，敌人攻击
                        damageReceived = maxOf(1, monster.attack - (currentPlayer?.defense ?: 0))
                        currentPlayerHp -= damageReceived
                        withContext(Dispatchers.Main) {
                            logGameEvent("回答错误！")
                            logGameEvent("${monster.name} 对你造成了 $damageReceived 点伤害！")
                        }
                        battleRound.is_correct = false
                    }

                    // 更新回合记录
                    battleRound.damage_dealt = damageDealt
                    battleRound.damage_received = damageReceived
                    battleRound.player_hp_after = currentPlayerHp
                    battleRound.monster_hp_after = currentEnemyHp

                    // 保存回合记录
                    withContext(Dispatchers.IO) {
                        battleRoundDao.insert(battleRound)
                    }

                    // 更新玩家生命值
                    currentPlayer?.let { player ->
                        withContext(Dispatchers.IO) {
                            player.current_hp = currentPlayerHp
                            playerDao.update(player)
                        }
                        withContext(Dispatchers.Main) {
                            updatePlayerUI()
                        }
                    }

                    roundCount++
                    delay(1000) // 延迟一秒，让玩家有时间阅读战斗信息

                    // 检查是否有一方已经失败
                    if (currentPlayerHp <= 0 || currentEnemyHp <= 0) {
                        break
                    }
                }
                
                // 战斗结束，更新战斗记录
                val battleResult = when {
                    currentPlayerHp <= 0 -> "DEFEAT"
                    currentEnemyHp <= 0 -> "VICTORY"
                    else -> "ESCAPE"
                }
                
                val expGained = if (battleResult == "VICTORY") monster.exp_reward else 0
                val goldGained = if (battleResult == "VICTORY") monster.gold_reward else 0
                
                withContext(Dispatchers.IO) {
                    // 更新战斗记录
                    battleRecord.end_time = Date()
                    battleRecord.result = battleResult
                    battleRecord.exp_gained = expGained
                    battleRecord.gold_gained = goldGained
                    battleRecordDao.update(battleRecord)
                }
                
                // 战斗结束
                withContext(Dispatchers.Main) {
                    logGameEvent("\n===== 战斗结束 =====")
                    
                    when (battleResult) {
                        "VICTORY" -> handleBattleVictory(monster)
                        "DEFEAT" -> handleBattleDefeat()
                        else -> logGameEvent("战斗中止")
                    }
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "战斗过程中出错: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GameActivity, "战斗过程中出错", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 判断答案是否正确
     * @param playerAnswer 玩家的答案
     * @param question 英语问题对象
     * @return 是否正确
     */
    private suspend fun isAnswerCorrect(playerAnswer: String, question: SpanishQuestion): Boolean {
        if (playerAnswer.isEmpty()) return false
        
        return try {
            when (question.type) {
                "FILL_BLANK" -> {
                    // 从填空题表获取正确答案
                    val fillBlankQuestion = withContext(Dispatchers.IO) {
                        fillBlankQuestionDao.findByQuestionId(question.question_id)
                    } ?: return false
                    
                    // 忽略大小写比较答案
                    playerAnswer.equals(fillBlankQuestion.correct_answer, ignoreCase = true)
                }
                "MULTIPLE_CHOICE" -> {
                    // 从选择题表获取正确答案
                    val choiceQuestion = withContext(Dispatchers.IO) {
                        choiceQuestionDao.findByQuestionId(question.question_id)
                    } ?: return false
                    
                    // 获取选项列表
                    val options = listOf(
                        choiceQuestion.option_a,
                        choiceQuestion.option_b,
                        choiceQuestion.option_c,
                        choiceQuestion.option_d
                    )
                    
                    // 获取正确选项的索引（A=0, B=1, C=2, D=3）
                    val correctIndex = when (choiceQuestion.correct_option.uppercase()) {
                        "A" -> 0
                        "B" -> 1
                        "C" -> 2
                        "D" -> 3
                        else -> return false
                    }
                    
                    // 比较玩家答案和正确选项
                    playerAnswer == options[correctIndex]
                }
                else -> false
            }
        } catch (e: Exception) {
            Log.e(TAG, "检查答案时出错: ${e.message}")
            false
        }
    }

    /**
     * 处理战斗失败
     */
    private fun handleBattleDefeat() {
        launch {
            try {
                withContext(Dispatchers.Main) {
                    logGameEvent("你被打败了...")
                    
                    // 返回村庄并恢复一半生命值
                    currentPlayer?.let { player ->
                        withContext(Dispatchers.IO) {
                            player.current_hp = player.max_hp / 2
                            playerDao.update(player)
                        }
                        
                        withContext(Dispatchers.Main) {
                            updatePlayerUI()
                            returnToVillage()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "处理战斗失败时出错: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GameActivity, "返回村庄失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    /**
     * 发现宝藏
     */
    private fun findTreasure() {
        try {
            // 根据玩家等级决定宝藏品质
            val playerLevel = currentPlayer?.level ?: 1
            
            // 宝藏类型
            val treasureTypes = listOf("金币", "药水", "装备", "宝石")
            val treasureType = treasureTypes.random()
            
            when (treasureType) {
                "金币" -> {
                    // 发现金币
                    val goldAmount = playerLevel * 15 + Random.nextInt(10, 50)
                    
                    currentPlayer?.let { player ->
                        launch {
                            withContext(Dispatchers.IO) {
                                player.gold += goldAmount
                                playerDao.update(player)
                                
                                // 更新成就 - 累计金币
                                checkAchievement("收集100金币", min(100, player.gold), player.player_id)
                                checkAchievement("收集1000金币", min(100, player.gold / 10), player.player_id)
                            }
                            
                            withContext(Dispatchers.Main) {
                                updatePlayerUI()
                                logGameEvent("你发现了一个小宝箱！获得了${goldAmount}金币。")
                            }
                        }
                    }
                }
                "药水" -> {
                    // 发现药水
                    val healAmount = playerLevel * 10 + Random.nextInt(10, 30)
                    
                    currentPlayer?.let { player ->
                        launch {
                            withContext(Dispatchers.IO) {
                                player.current_hp = min(player.current_hp + healAmount, player.max_hp)
                                playerDao.update(player)
                            }
                            
                            withContext(Dispatchers.Main) {
                                updatePlayerUI()
                                logGameEvent("你发现了一瓶治疗药水！恢复了${healAmount}点生命值。")
                            }
                        }
                    }
                }
                "装备" -> {
                    // 发现装备（暂时只增加属性，未来可以实现真正的装备系统）
                    val equipNames = listOf("铁剑", "皮革护甲", "木盾", "银戒指", "铜项链")
                    val equipName = equipNames.random()
                    
                    val statBoost = Random.nextInt(1, 5)
                    
                    currentPlayer?.let { player ->
                        launch {
                            withContext(Dispatchers.IO) {
                                player.attack += statBoost
                                playerDao.update(player)
                            }
                            
                            withContext(Dispatchers.Main) {
                                updatePlayerUI()
                                logGameEvent("你发现了一件装备：$equipName！攻击力提升了${statBoost}点。")
                            }
                        }
                    }
                }
                "宝石" -> {
                    // 发现宝石（增加经验值）
                    val expAmount = playerLevel * 25 + Random.nextInt(15, 40)
                    
                    currentPlayer?.let { player ->
                        launch {
                            withContext(Dispatchers.IO) {
                                player.exp += expAmount
                                
                                // 检查是否升级
                                checkLevelUp(player)
                                
                                playerDao.update(player)
                            }
                            
                            withContext(Dispatchers.Main) {
                                updatePlayerUI()
                                logGameEvent("你发现了一颗闪亮的宝石！获得了${expAmount}点经验值。")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "生成宝藏失败: ${e.message}")
            logGameEvent("你似乎发现了什么闪闪发光的东西，但它转眼就消失了。")
        }
    }
    
    /**
     * 发现事件
     */
    private fun discoveryEvent() {
        try {
            // 随机事件
            val events = listOf(
                "你发现了一个古老的石碑，上面刻着奇怪的符文。也许这是某种谜题的线索。",
                "你在地上发现了一些动物的脚印，它们似乎通向某个方向。",
                "你看到远处有一缕烟，可能是有人在那里生活。",
                "你发现了一些奇怪的植物，它们发出微弱的光芒。",
                "你听到附近有流水的声音，但看不到任何水源。",
                "空气中弥漫着一种奇怪的香气，似乎让你感到异常清醒。",
                "你发现了一本破旧的日记，但只能辨认出几个词语。",
                "你在地上发现了一枚古老的硬币，上面的图案你从未见过。",
                "一只小鸟落在你的肩膀上，叽叽喳喳地叫了几声就飞走了。",
                "你看到了远处有一座奇怪的建筑，但太远了看不清楚。"
            )
            
            val randomEvent = events.random()
            logGameEvent(randomEvent)
            
            // 有30%的几率获得特殊效果
            if (Random.nextInt(100) < 30) {
                currentPlayer?.let { player ->
                    val effects = listOf(
                        "你感觉到一股神秘的力量流入你的体内。(最大生命值+10)",
                        "你的思维变得更加敏锐。(攻击力+2)",
                        "你的皮肤变得更加坚韧。(防御力+2)"
                    )
                    
                    val randomEffect = Random.nextInt(3)
                    
                    launch {
                        withContext(Dispatchers.IO) {
                            when (randomEffect) {
                                0 -> {
                                    player.max_hp += 10
                                    player.current_hp += 10
                                }
                                1 -> player.attack += 2
                                2 -> player.defense += 2
                            }
                            
                            playerDao.update(player)
                        }
                        
                        withContext(Dispatchers.Main) {
                            updatePlayerUI()
                            logGameEvent(effects[randomEffect])
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "生成发现事件失败: ${e.message}")
            logGameEvent("你感觉这个地方有些不同寻常，但说不出是什么。")
        }
    }
    
    /**
     * 检查两个节点是否相邻
     * 地图大小为50*50，坐标从(0,0)到(50,50)
     */
    private fun areNodesAdjacent(node1: MapNode, node2: MapNode): Boolean {
        val x1 = node1.x_coordinate ?: return false
        val y1 = node1.y_coordinate ?: return false
        val x2 = node2.x_coordinate ?: return false
        val y2 = node2.y_coordinate ?: return false
        
        // 检查坐标是否在有效范围内
        if (x1 !in 0..50 || y1 !in 0..50 || x2 !in 0..50 || y2 !in 0..50) {
            return false
        }
        
        // 计算坐标差值
        val dx = x1 - x2
        val dy = y1 - y2
        
        // 检查是否在相邻的8个方向之一（相邻格子）
        return dx in -1..1 && dy in -1..1 && !(dx == 0 && dy == 0)
    }

    /**
     * 根据两个节点的相对位置确定方向
     * 地图大小为50*50，坐标从(0,0)到(50,50)
     */
    private fun getDirectionBetweenNodes(fromNode: MapNode, toNode: MapNode): String? {
        val x1 = fromNode.x_coordinate ?: return null
        val y1 = fromNode.y_coordinate ?: return null
        val x2 = toNode.x_coordinate ?: return null
        val y2 = toNode.y_coordinate ?: return null
        
        // 检查坐标是否在有效范围内
        if (x1 !in 0..50 || y1 !in 0..50 || x2 !in 0..50 || y2 !in 0..50) {
            return null
        }
        
        // 计算坐标差值
        val dx = x2 - x1
        val dy = y2 - y1
        
        return when {
            dx == 0 && dy == -1 -> "北"
            dx == 1 && dy == -1 -> "东北"
            dx == 1 && dy == 0 -> "东"
            dx == 1 && dy == 1 -> "东南"
            dx == 0 && dy == 1 -> "南"
            dx == -1 && dy == 1 -> "西南"
            dx == -1 && dy == 0 -> "西"
            dx == -1 && dy == -1 -> "西北"
            else -> null
        }
    }

    /**
     * 获取相反方向
     */
    private fun getOppositeDirection(direction: String): String {
        return when (direction) {
            "北" -> "南"
            "东北" -> "西南"
            "东" -> "西"
            "东南" -> "西北"
            "南" -> "北"
            "西南" -> "东北"
            "西" -> "东"
            "西北" -> "东南"
            else -> direction
        }
    }

    /**
     * 发现新路径
     */
    private fun findNewPath() {
        launch {
            try {
                // 获取当前节点
                val currentNode = currentNode ?: return@launch
                
                // 检查当前节点坐标是否有效
                val x = currentNode.x_coordinate
                val y = currentNode.y_coordinate
                if (x == null || y == null || x !in 0..50 || y !in 0..50) {
                    logGameEvent("当前位置的坐标似乎有些问题，无法探索新的路径。")
                    return@launch
                }
                
                // 从数据库获取所有节点
                val allNodes = withContext(Dispatchers.IO) {
                    mapNodeDao.findByMapId(currentMapId)
                }
                
                // 获取当前已连接的方向
                val connectedDirections = availableConnections.mapNotNull { it.direction }.toSet()
                
                // 所有可能的方向
                val allDirections = setOf("北", "东北", "东", "东南", "南", "西南", "西", "西北")
                
                // 如果所有方向都已连接，提示探索完毕
                if (connectedDirections.size >= 8) {
                    logGameEvent("你探索了周围的区域，但所有的路径你都已经发现了。")
                    return@launch
                }
                
                // 过滤出相邻但未连接的节点
                val connectedNodeIds = availableConnections.map { it.to_node_id }
                val adjacentNodes = allNodes.filter { node -> 
                    node.node_id != currentNodeId && 
                    !connectedNodeIds.contains(node.node_id) &&
                    areNodesAdjacent(currentNode, node)
                }
                
                if (adjacentNodes.isEmpty()) {
                    // 没有相邻节点，创建新节点
                    // 获取未使用的方向
                    val availableDirections = allDirections - connectedDirections
                    if (availableDirections.isEmpty()) {
                        logGameEvent("你探索了周围的区域，但所有的路径你都已经发现了。")
                        return@launch
                    }
                    
                    // 随机选择一个未使用的方向
                    val newDirection = availableDirections.random()
                    
                    // 根据方向计算新节点的坐标
                    val (newX, newY) = when (newDirection) {
                        "北" -> Pair(x, y - 1)
                        "东北" -> Pair(x + 1, y - 1)
                        "东" -> Pair(x + 1, y)
                        "东南" -> Pair(x + 1, y + 1)
                        "南" -> Pair(x, y + 1)
                        "西南" -> Pair(x - 1, y + 1)
                        "西" -> Pair(x - 1, y)
                        "西北" -> Pair(x - 1, y - 1)
                        else -> Pair(x, y)
                    }
                    
                    // 检查新坐标是否有效
                    if (newX !in 0..50 || newY !in 0..50) {
                        logGameEvent("这个方向似乎到达了地图的边界。")
                        return@launch
                    }
                    
                    // 创建新节点
                    val (nodeName, nodeDescription, nodeType) = generateRandomNodeInfo()
                    val newNode = MapNode(
                        node_id = 0,
                        map_id = currentMapId,
                        name = nodeName,
                        description = nodeDescription,
                        x_coordinate = newX,
                        y_coordinate = newY,
                        is_starting_point = false,
                        is_dragon_lair = false,
                        node_type = nodeType
                    )
                    
                    // 保存新节点
                    val newNodeId = withContext(Dispatchers.IO) {
                        mapNodeDao.insert(newNode)
                    }
                    
                    // 创建连接
                    createBidirectionalConnection(currentNodeId, newNodeId, newDirection)
                    
                    withContext(Dispatchers.Main) {
                        logGameEvent("你发现了一条通往${newDirection}方向的新路径！那里似乎是一片未知的区域。")
                    }
                } else {
                    // 有相邻节点，随机选择一个建立连接
                    val targetNode = adjacentNodes.random()
                    val direction = getDirectionBetweenNodes(currentNode, targetNode)
                    
                    if (direction != null) {
                        // 创建连接
                        createBidirectionalConnection(currentNodeId, targetNode.node_id, direction)
                        
                        withContext(Dispatchers.Main) {
                            logGameEvent("你发现了一条通往${direction}方向的新路径！")
                        }
                    }
                }
                
                // 重新加载连接
                withContext(Dispatchers.Main) {
                    loadNodeConnections()
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "创建新路径失败: ${e.message}")
                withContext(Dispatchers.Main) {
                    logGameEvent("你尝试寻找新的路径，但没有成功。")
                }
            }
        }
    }
    
    /**
     * 创建双向连接
     */
    private suspend fun createBidirectionalConnection(fromNodeId: Long, toNodeId: Long, direction: String) {
        withContext(Dispatchers.IO) {
            // 创建正向连接
            val newConnection = NodeConnection(
                connection_id = 0,
                from_node_id = fromNodeId,
                to_node_id = toNodeId,
                direction = direction,
                is_locked = Random.nextInt(100) < 30,
                unlock_condition = if (Random.nextInt(100) < 30) "需要击败守卫" else null
            )
            nodeConnectionDao.insert(newConnection)
            
            // 创建反向连接
            val oppositeDirection = getOppositeDirection(direction)
            val reverseConnection = NodeConnection(
                connection_id = 0,
                from_node_id = toNodeId,
                to_node_id = fromNodeId,
                direction = oppositeDirection,
                is_locked = newConnection.is_locked,
                unlock_condition = newConnection.unlock_condition
            )
            nodeConnectionDao.insert(reverseConnection)
            
            // 如果路径被锁定，添加提示
            if (newConnection.is_locked) {
                withContext(Dispatchers.Main) {
                    logGameEvent("但是这条路径似乎被什么东西挡住了...")
                }
            }
        }
    }

    /**
     * 检查并更新成就进度
     * @param achievementName 成就名称
     * @param progress 成就进度（0-100）
     * @param playerId 玩家ID
     */
    private suspend fun checkAchievement(achievementName: String, progress: Int, playerId: Long) {
        try {
            withContext(Dispatchers.IO) {
                // 查找对应的成就
                val achievement = achievementDao.findByName(achievementName)
                
                if (achievement != null) {
                    // 更新或创建成就进度
                    val playerAchievementId = playerAchievementDao.updateOrCreateAchievement(
                        playerId,
                        achievement.achievement_id,
                        progress
                    )
                    
                    // 检查成就是否已完成
                    if (progress >= 100) {
                        // 获取更新后的玩家成就关联
                        val playerAchievement = playerAchievementDao.findById(playerAchievementId)
                        
                        // 如果成就刚刚完成（之前未领取），通知玩家
                        if (playerAchievement != null && !playerAchievement.isClaimed) {
                            withContext(Dispatchers.Main) {
                                logGameEvent("成就解锁：${achievement.name}！")
                                logGameEvent(achievement.description)
                                
                                // 如果有奖励，发放奖励
                                if (achievement.reward_value > 0) {
                                    when (achievement.reward_type) {
                                        "gold" -> {
                                            // 发放金币奖励
                                            currentPlayer?.let { player ->
                                                player.gold += achievement.reward_value
                                                withContext(Dispatchers.IO) {
                                                    playerDao.update(player)
                                                }
                                                logGameEvent("获得奖励：${achievement.reward_value}金币")
                                            }
                                        }
                                        "exp" -> {
                                            // 发放经验奖励
                                            currentPlayer?.let { player ->
                                                player.exp += achievement.reward_value
                                                withContext(Dispatchers.IO) {
                                                    playerDao.update(player)
                                                    // 检查是否升级
                                                    checkLevelUp(player)
                                                }
                                                logGameEvent("获得奖励：${achievement.reward_value}经验")
                                            }
                                        }
                                        "hp" -> {
                                            // 发放生命值上限奖励
                                            currentPlayer?.let { player ->
                                                player.max_hp += achievement.reward_value
                                                player.current_hp += achievement.reward_value
                                                withContext(Dispatchers.IO) {
                                                    playerDao.update(player)
                                                }
                                                logGameEvent("获得奖励：最大生命值+${achievement.reward_value}")
                                            }
                                        }
                                        "attack" -> {
                                            // 发放攻击力奖励
                                            currentPlayer?.let { player ->
                                                player.attack += achievement.reward_value
                                                withContext(Dispatchers.IO) {
                                                    playerDao.update(player)
                                                }
                                                logGameEvent("获得奖励：攻击力+${achievement.reward_value}")
                                            }
                                        }
                                        "defense" -> {
                                            // 发放防御力奖励
                                            currentPlayer?.let { player ->
                                                player.defense += achievement.reward_value
                                                withContext(Dispatchers.IO) {
                                                    playerDao.update(player)
                                                }
                                                logGameEvent("获得奖励：防御力+${achievement.reward_value}")
                                            }
                                        }
                                    }
                                    
                                    // 更新玩家UI
                                    updatePlayerUI()
                                }
                                
                                // 标记成就为已领取
                                withContext(Dispatchers.IO) {
                                    playerAchievementDao.markAsClaimed(playerAchievementId)
                                }
                            }
                        }
                    }
                } else {
                    Log.w(TAG, "找不到成就: $achievementName")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "检查成就失败: $achievementName, ${e.message}", e)
        }
    }
    
    /**
     * 显示成就列表
     */
    private fun showAchievements() {
        currentPlayer?.let { player ->
            launch {
                try {
                    // 获取玩家成就详情
                    val achievements = withContext(Dispatchers.IO) {
                        playerAchievementDao.getPlayerAchievementDetails(player.player_id)
                    }
                    
                    // 构建成就列表字符串
                    val achievementText = StringBuilder()
                    for (achievement in achievements) {
                        val progressText = if (achievement.progress >= 100) {
                            if (achievement.is_claimed) "✓ 已完成" else "☑ 已解锁（未领取）"
                        } else {
                            "${achievement.progress}%"
                        }
                        
                        achievementText.append("${achievement.name}: $progressText\n")
                        achievementText.append("  ${achievement.description}\n\n")
                    }
                    
                    // 显示成就对话框
                    withContext(Dispatchers.Main) {
                        AlertDialog.Builder(this@GameActivity)
                            .setTitle("成就列表")
                            .setMessage(if (achievementText.isNotEmpty()) achievementText.toString() else "暂无成就记录")
                            .setPositiveButton("关闭", null)
                            .show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "显示成就列表失败: ${e.message}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@GameActivity, "无法加载成就列表", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    
    /**
     * 在游戏开始时初始化成就系统
     */
    private fun initAchievements() {
        currentPlayer?.let { player ->
            launch {
                try {
                    // 检查与玩家相关的成就进度
                    withContext(Dispatchers.IO) {
                        // 等级成就
                        checkAchievement("达到等级5", if (player.level >= 5) 100 else player.level * 20, player.player_id)
                        checkAchievement("达到等级10", if (player.level >= 10) 100 else player.level * 10, player.player_id)
                        checkAchievement("达到等级20", if (player.level >= 20) 100 else player.level * 5, player.player_id)
                        
                        // 金币成就
                        checkAchievement("收集100金币", min(100, player.gold), player.player_id)
                        checkAchievement("收集1000金币", min(100, player.gold / 10), player.player_id)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "初始化成就失败: ${e.message}")
                }
            }
        }
    }

    /**
     * 显示地图信息
     */
    private fun showMapInfo() {
        currentNode?.let { node ->
            val mapInfo = StringBuilder()
            mapInfo.append("当前位置：${node.name}\n\n")
            mapInfo.append("坐标：(${node.x_coordinate ?: '?'}, ${node.y_coordinate ?: '?'})\n\n")
            mapInfo.append("地形描述：\n${node.description}\n\n")
            
            // 显示可用的方向
            val directions = availableConnections.mapNotNull { connection ->
                val targetNode = runBlocking {
                    withContext(Dispatchers.IO) {
                        mapNodeDao.findById(connection.to_node_id)
                    }
                }
                if (targetNode != null) {
                    "${connection.direction}：${targetNode.name}" + 
                    if (connection.is_locked) " 🔒" else ""
                } else null
            }
            
            if (directions.isNotEmpty()) {
                mapInfo.append("可通行方向：\n")
                directions.forEach { direction ->
                    mapInfo.append("$direction\n")
                }
            }

            AlertDialog.Builder(this)
                .setTitle("地图信息")
                .setMessage(mapInfo.toString())
                .setPositiveButton("关闭", null)
                .show()
        } ?: run {
            Toast.makeText(this, "无法获取当前位置信息", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 生成随机的节点信息
     * @return Triple<节点名称, 节点描述, 节点类型>
     */
    private fun generateRandomNodeInfo(): Triple<String, String, String> {
        val locationTypes = listOf(
            Triple("森林", listOf(
                "这片森林郁郁葱葱，树冠遮天蔽日。空气中弥漫着青草和野花的芳香，偶尔能听到鸟儿的啼鸣。",
                "一片宁静的林地，古老的橡树和松树并肩而立。地面上铺满了厚厚的落叶，踩上去发出沙沙的响声。",
                "这里的树木稀疏一些，阳光透过树叶在地面上投下斑驳的光影。周围点缀着各种野生蘑菇和浆果。"
            ), "forest"),
            Triple("草地", listOf(
                "一片开阔的草地，青草在微风中轻轻摇曳。远处能看到零星的野花点缀其中。",
                "这片草地上开满了各色野花，蝴蝶在花丛中翩翩起舞。空气中充满了花香。",
                "绿意盎然的草地上散布着几块大石头，适合休息。周围能听到昆虫的鸣叫声。"
            ), "grassland"),
            Triple("山丘", listOf(
                "一座不高的山丘，从这里可以俯瞰周围的景色。山坡上长满了矮小的灌木。",
                "这座小山丘的岩石上长满了青苔，看起来年代久远。山顶上有一块特别大的岩石。",
                "缓坡的山丘，适合攀爬。山坡上零星分布着一些岩石和野花。"
            ), "hill"),
            Triple("溪流", listOf(
                "一条清澈的小溪从这里流过，水流清冽。溪边生长着各种水生植物。",
                "潺潺的溪水声不绝于耳，几块光滑的大石头横卧在溪水中，可以踩着过去。",
                "这条小溪的水很浅，能看到溪底的卵石。岸边长着一些芦苇。"
            ), "stream"),
            Triple("废墟", listOf(
                "这里有一些古老建筑的遗迹，石墙上爬满了藤蔓。废墟中弥漫着神秘的气息。",
                "几根断裂的石柱静静地矗立在这里，诉说着往昔的辉煌。地上散落着一些石块。",
                "一片残垣断壁，似乎是某个古老建筑的一部分。墙缝中长出了野花。"
            ), "ruins"),
            Triple("洞穴", listOf(
                "一个不大的山洞入口，洞口周围长满了藤蔓。从里面传来阵阵凉风。",
                "这个天然形成的洞穴看起来很干燥。洞口边有一些奇特的岩石。",
                "洞穴入口不高，需要弯腰才能进去。洞壁上有一些闪闪发光的矿物。"
            ), "cave"),
            Triple("沼泽", listOf(
                "这片小沼泽地上长满了水生植物，空气中有些潮湿。要小心脚下的淤泥。",
                "沼泽地的水面上漂浮着一些睡莲，周围生长着茂密的芦苇。",
                "这片沼泽地看起来有些阴森，但实际上栖息着很多小生物。"
            ), "swamp"),
            Triple("果林", listOf(
                "这是一片野生果树林，树上挂着一些野果。空气中飘着果香。",
                "几棵果树零星分布在这里，枝头上结着各种果实。地上落着一些熟透的水果。",
                "这片果林似乎是很久以前有人种植的，现在已经野化了。"
            ), "orchard")
        )
        
        val (locationType, descriptions, nodeType) = locationTypes.random()
        val prefix = listOf(
            "神秘的", "宁静的", "荒芜的", "幽深的", "古老的",
            "隐秘的", "茂密的", "空旷的", "迷人的", "荒凉的"
        ).random()
        
        val suffix = listOf(
            "小径", "地带", "区域", "角落", "所在",
            "地点", "位置", "场所", "境地", "要道"
        ).random()
        
        return Triple(
            "$prefix$locationType$suffix",
            descriptions.random(),
            nodeType
        )
    }

    /**
     * 显示问题对话框
     * @param question 问题
     * @return 玩家的答案
     */
    private suspend fun showQuestionDialog(question: SpanishQuestion): String = suspendCoroutine { continuation ->
        launch {
            try {

                val dialogView = when (question.type) {
                    "MULTIPLE_CHOICE" -> {
                        // 获取选择题选项
                        val choiceQuestion = withContext(Dispatchers.IO) {
                            choiceQuestionDao.findByQuestionId(question.question_id)
                        } ?: run {
                            continuation.resume("")
                            return@launch
                        }

                        // 使用单选按钮组布局
                        layoutInflater.inflate(R.layout.dialog_choice_question, null).apply {
                            val questionTextView = findViewById<TextView>(R.id.questionTextView)
                            val optionsRadioGroup = findViewById<RadioGroup>(R.id.optionsRadioGroup)
                            
                            questionTextView.text = question.content
                            
                            // 添加选项
                            listOf(
                                choiceQuestion.option_a,
                                choiceQuestion.option_b,
                                choiceQuestion.option_c,
                                choiceQuestion.option_d
                            ).forEachIndexed { index, option ->
                                val radioButton = RadioButton(this@GameActivity).apply {
                                    id = View.generateViewId()
                                    text = option
                                    layoutParams = RadioGroup.LayoutParams(
                                        RadioGroup.LayoutParams.MATCH_PARENT,
                                        RadioGroup.LayoutParams.WRAP_CONTENT
                                    )
                                }
                                optionsRadioGroup.addView(radioButton)
                            }
                        }
                    }
                    "FILL_BLANK" -> {
                        // 获取填空题信息
                        val fillBlankQuestion = withContext(Dispatchers.IO) {
                            fillBlankQuestionDao.findByQuestionId(question.question_id)
                        }

                        // 使用文本输入布局
                        layoutInflater.inflate(R.layout.dialog_fill_blank_question, null).apply {
                            val questionTextView = findViewById<TextView>(R.id.questionTextView)
                            val answerEditText = findViewById<EditText>(R.id.answerEditText)

                            if (fillBlankQuestion != null) {
                                questionTextView.text = fillBlankQuestion.sentence_with_blank
                            }
                            // 如果有提示，显示提示信息
                            fillBlankQuestion?.hint?.let { hint ->
                                answerEditText.hint = "提示：$hint"
                            }
                        }
                    }
                    else -> {
                        continuation.resume("")
                        return@launch
                    }
                }

                // 在主线程显示对话框
                withContext(Dispatchers.Main) {
                    AlertDialog.Builder(this@GameActivity)
                        .setTitle("回答问题")
                        .setView(dialogView)
                        .setCancelable(false)
                        .setPositiveButton("提交") { _, _ ->
                            when (question.type) {
                                "MULTIPLE_CHOICE" -> {
                                    val optionsRadioGroup = dialogView.findViewById<RadioGroup>(R.id.optionsRadioGroup)
                                    val selectedId = optionsRadioGroup.checkedRadioButtonId
                                    if (selectedId != -1) {
                                        val selectedButton = dialogView.findViewById<RadioButton>(selectedId)
                                        continuation.resume(selectedButton.text.toString())
                                    } else {
                                        continuation.resume("")
                                    }
                                }
                                "FILL_BLANK" -> {
                                    val answerEditText = dialogView.findViewById<EditText>(R.id.answerEditText)
                                    continuation.resume(answerEditText.text.toString().trim())
                                }
                                else -> continuation.resume("")
                            }
                        }
                        .setNegativeButton("跳过") { _, _ ->
                            continuation.resume("")
                        }
                        .create()
                        .show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "显示问题对话框失败: ${e.message}")
                continuation.resume("")
            }
        }
    }

    /**
     * 探索区域
     */
    private fun exploreArea() {
        try {
            // 检查玩家健康状态
            if (currentPlayer?.current_hp ?: 0 <= 0) {
                Toast.makeText(this, "你已经失去了战斗能力，需要恢复健康才能探索", Toast.LENGTH_SHORT).show()
                return
            }
            
            // 创建一个加载对话框
            val loadingDialog = AlertDialog.Builder(this)
                .setMessage("正在探索区域...")
                .setCancelable(false)
                .create()
            loadingDialog.show()
            
            // 计算当前节点的连接数
            launch {
                try {
                    val currentNodeId = currentNode?.node_id ?: return@launch
                    val connections = nodeConnectionDao.findConnectionsFromNode(currentNodeId)
                    val connectionCount = connections.size
                    
                    // 根据连接数调整概率
                    // 最大可能的连接数是8（八个方向）
                    val maxConnections = 8
                    val remainingConnections = maxConnections - connectionCount
                    
                    // 计算新路径的基础概率（连接越少，概率越高）
                    // 当没有连接时，新路径概率为75%
                    // 当连接数达到最大时，新路径概率为15%
                    val newPathBaseProb = (remainingConnections.toFloat() / maxConnections * 60 + 15).toInt()
                    
                    // 其他事件的概率相应调整
                    val enemyProb = (50 * (connectionCount.toFloat() / maxConnections)).toInt() + 20  // 最低20%，最高70%
                    val treasureProb = 10  // 保持不变
                    val eventProb = 15     // 保持不变
                    
                    // 使用Handler在主线程更新UI
                    Handler(Looper.getMainLooper()).postDelayed({
                        loadingDialog.dismiss()
                        
                        // 随机确定探索结果
                        val exploreResult = Random.nextInt(100)
                        
                        when {
                            // 遇到敌人
                            exploreResult < enemyProb -> {
                                encounterEnemy()
                            }
                            // 发现宝藏
                            exploreResult < enemyProb + treasureProb -> {
                                findTreasure()
                            }
                            // 发现事件
                            exploreResult < enemyProb + treasureProb + eventProb -> {
                                discoveryEvent()
                            }
                            // 找到新路径
                            else -> {
                                findNewPath()
                            }
                        }
                        
                        // 更新UI
                        loadAndDisplayNodeContent()
                        
                    }, 500) // 0.5秒的探索时间
                    
                } catch (e: Exception) {
                    Log.e(TAG, "获取节点连接数时出错: ${e.message}")
                    loadingDialog.dismiss()
                    runOnUiThread {
                        Toast.makeText(this@GameActivity, "探索区域时出错", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "探索区域时出错: ${e.message}")
            Toast.makeText(this, "探索区域时出错", Toast.LENGTH_SHORT).show()
        }
    }
} 
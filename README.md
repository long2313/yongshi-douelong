# 勇士斗恶龙 · 西语版

一款把**西班牙语词汇学习**和 **RPG 冒险**缝在一起的 Android 单机游戏。

答题就是战斗：答对了砍怪，答错了掉血。地图、怪物、Boss、装备、成就、存档，一整套都做齐了。

- 包名：`com.yongshi.douelong`
- 版本：1.0（versionCode 1）
- 支持：Android 7.0（API 24）及以上
- 技术栈：Kotlin 2.0.21 / AGP 8.9.1 / Gradle 8.11.1 / Room 2.6.1 / Coroutines

---

## 关于这个项目

我想做一个「背单词不那么痛苦」的东西。

市面上的西语 App 大多是卡片翻来翻去，翻完就忘。而 RPG 里为了打过一个 Boss，玩家会心甘情愿地重复几十遍，因为每次重复都有即时反馈——打中了、掉血了、升级了。

所以我把两者缝在一起：**你想往地图深处走，就得把题答对。** 答错不是「再想想」，是直接掉血，血空了就得回村。

目前项目还很粗糙，内容量也不算多，但主线是通的——能建角色、能走地图、能遇怪、能答题战斗、能打败恶龙、能存档读档。

## 玩法

**一局游戏是这样的：**

1. 进游戏建角色，从新手村广场出发
2. 在地图上走到不同节点，节点分普通、战斗、Boss 三类
3. 遇到战斗，题目弹出来——**答对，你打它；答错，它打你**
4. 打赢了拿经验和金币，升级涨属性
5. 攒够等级和装备，去挑最终 Boss 恶龙

除了主线，还有探索、寻宝、发现新路径这些支线：地图不是一开始就全亮的，有些路要自己踩出来。

## 内容

### 题库

`app/src/main/assets/question_inserts.sql` — 196 道题，全部是**西班牙语软件测试术语**，难度 1–3。

| 表 | 题数 | 题型 |
|---|---|---|
| `spanish_question` | 103 | 西语术语 → 中文释义 |
| `choice_question` | 98 | 四选一 |
| `fill_blank_question` | 4 | 填空 |

题目统一归属 `category = 'SOFTWARE_TEST'`。举个例子：

```sql
VALUES (1, '¿Cuál es la traducción china correcta de "Mantenimiento"?',
        'MULTIPLE_CHOICE', 1, 'SOFTWARE_TEST')
```

题库在数据库第一次创建时由 `database/util/SqlScriptExecutor.kt` 读入，入口见 `GameDatabase.kt:158`。

### 地图与节点

开局写死三张地图，各管一个阶段：

| 地图 | 名称 | 定位 | 等级要求 |
|---|---|---|---|
| 1 | 知识之岛 | 新手入门的西语冒险岛 | 1 |
| 2 | 智慧群山 | 进阶语法挑战区 | 5 |
| 3 | 学习谷地 | 词汇积累专属地图 | 3 |

对应的节点：

| 节点 | 所属地图 | 名称 | 类型 |
|---|---|---|---|
| 1 | 1 | 新手村广场 | NORMAL（出生点） |
| 2 | 1 | 语法森林 | BATTLE |
| 3 | 2 | 词汇山峰 | BOSS |

写入逻辑在 `App.kt` 的 `initMapAndNodeDataSync()`，幂等——已有数据就跳过，不会重复插。

### 角色

新角色从 `map_node` 里取 1 号节点当出生点（取不到就退而取第一个节点），地图 ID 跟着节点走；一个节点都没有时给个提示就退出，不会静默崩掉。

初始属性：**攻击 10 / 防御 5**，之后靠升级和装备涨。



## 目录结构

```
app/src/main/
├─ java/com/yongshi/douelong/
│  ├─ App.kt                    Application，启动时初始化地图/节点
│  ├─ MainActivity.kt           入口，检查存档
│  ├─ GameActivity.kt           全部玩法（2700+ 行）
│  └─ database/
│     ├─ GameDatabase.kt        Room 数据库 + 建库时导题库
│     ├─ GameWorldGenerator.kt  随机世界生成（备用，当前开局不用）
│     ├─ dao/                   18 个 DAO
│     ├─ model/                 18 个实体
│     └─ util/SqlScriptExecutor.kt  执行 assets 里的 SQL
├─ assets/question_inserts.sql  196 道西语题
└─ res/                         布局、图标、样式
```

数据库 18 张表：`player` `monster` `item` `equipment` `map` `map_node` `node_connection` `node_event` `achievement` `battle_record` `battle_round` `spanish_question` `choice_question` `fill_blank_question` `monster_drop` `monster_question` `game_save` `game_setting`。

## 怎么加内容

**加题**：直接往 `assets/question_inserts.sql` 里追加 `INSERT` 语句，注意三张题表各自的字段结构不同（选项题要多插选项行）。改完把 APK 重装一次，数据库重建时会自动导入。

**加地图/节点**：照着 `App.kt` 里 `initMapAndNodeDataSync()` 的写法加 `Map(...)` / `MapNode(...)` 即可，节点类型用 `NORMAL` / `BATTLE` / `BOSS`。

**想让世界每次都不一样**：`GameWorldGenerator.kt` 还在，把 `createNewPlayer()` 里取预置节点那段换成调 `GameWorldGenerator.generateNewWorld()` 就能恢复随机世界。

## 已知问题

- 预置数据只有 3 个节点，探索 / 寻路类的内容量偏少，玩起来偏短。

## 协议

MIT License，详见 [LICENSE](LICENSE)。

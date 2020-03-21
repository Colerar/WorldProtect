# WorldProtect
## 介绍
这是一个高度可配置化的世界保护插件, 总之, 它面面俱到.
## 安装
您可在 Release 中下载打包好的 .jar 文件, 再将其放入 plugins 文件夹. 同时, 请参见 [依赖](https://github.com/Colerar/WorldProtect/new/master?readme=1#%E4%BE%9D%E8%B5%96) 章,
## 使用
本插件使用 EasyAPI 提供的便捷配置. 
当前 WorldProtect 支持以下自定义参数:
- 可否交互
- 可否与实体交互
- 可否坐上载具
- 可否更改游戏模式
- 切换世界后默认游戏模式
- 可否投掷
- 可否丢物品
- 可否被伤害
- 饥饿值可否更改
- 可否爆炸
- 可否交换物品
- 可否放置
- 可否破坏
- 方块可否被焚尽
- 方块可否点燃
- 方块可否生长
- 流体可否流动
- 树叶可否衰退
- 天气可否改变
- 禁止物品列表
- 禁止命令列表
- 白名单

您可以直接输入 /wp gui 以打开 GUI 配置界面. 

对于白名单和禁止物品列表, 您可以通过指令操作:
- ```/wp banitem <add|remove> [world: World] [itemId: itemId] ``` 
  - add 添加 remove 移除
  - world 即你世界的名称, 不输入默认当前世界.
  - itemId 即要操作的物品, 不输入默认手持物品(若有).
- ```/wp whitelist <add|remove> [world: World] [player: Target]```
  - add 添加 remove 移除
  - world 即你世界的名称, 不输入默认当前世界.
  - player 即要操作的玩家, 不输入默认为自己.
- ```/wp bancommand <add|remove> <command: String> [world: String]```
  - add 添加 remove 移除
  - command 即要操作的指令.
    - 实质上, 你可以输入一个正则表达式, 一般而言, 你只需要输入文本即可.
    - 你可以输入主命令 (例如: ```/tp```), 你也可以输入子命令(```/wmg add```), 但是你需要添加双引号--  ```"/wmg add"```
    - 一般地，推荐输入形如这样的: ```"^/主命令(\s+|$)"``` , 意思是匹配开头为 主命令 后面要么空格要么终止的命令, 举个例子, ```/tp``` 不会匹配 ```/tpa``` 但是会匹配 ```/tp Steve```
  - world 即你世界的名称, 不输入默认当前世界.
## 依赖
本插件依赖于:
- [KotlinLib](https://nukkitx.com/resources/kotlinlib.48/)
- [GUI](https://github.com/Him188/GUI)
- [EasyAPI](https://github.com/WetABQ/EasyAPI-Nukkit)

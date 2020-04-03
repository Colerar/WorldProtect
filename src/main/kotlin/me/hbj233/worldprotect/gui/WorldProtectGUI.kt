package me.hbj233.worldprotect.gui

import me.hbj233.worldprotect.WorldProtectPlugin
import me.hbj233.worldprotect.module.WorldProtectModule
import moe.him188.gui.window.ResponsibleFormWindowSimple
import top.wetabq.easyapi.gui.ConfigGUI
import top.wetabq.easyapi.utils.color

class WorldProtectConfigGui : ResponsibleFormWindowSimple("WorldProtect 配置面板", "&e&l请选择一个世界以修改".color()) {

    init {

        WorldProtectPlugin.instance.server.levels.values.forEach {
            val wConfig = WorldProtectModule.worldProtectConfig

            this.addButton(it.folderName) { player ->
                val configGUI = ConfigGUI(
                        wConfig,
                        wConfig.simpleConfig[it.folderName]?:wConfig.getDefaultValue(),
                        player.level.folderName,
                        "&c&l${player.level.folderName} &r&e的世界保护配置".color(),
                        this
                )

                configGUI.setTranslateMap(linkedMapOf(
                        "canInteract" to "可否交互",
                        "canInteractEntity" to "可否与实体交互",
                        "canPlayerEnterVehicle" to "可否坐上载具",
                        "canChangeGamemode" to "可否更改游戏模式",
                        "defaultGamemode" to "切换世界后默认游戏模式",
                        "canProjLaunch" to "可否投掷",
                        "canDropItem" to "可否丢物品",
                        "canBeDamaged" to "可否被伤害",
                        "canFoodLevelChange" to "饥饿值可否更改",
                        "canExplosion" to "可否爆炸",
                        "canInventoryTransaction" to "可否交换物品",
                        "canPlace" to "可否放置",
                        "canBreak" to "可否破坏",
                        "canBurn" to "方块可否被焚尽",
                        "canIgnite" to "方块可否点燃",
                        "canBlockForm" to "方块可否生长",
                        "canLiquidFlow" to "流体可否流动",
                        "canLeavesDecay" to "树叶可否衰退",
                        "canWeatherChange" to "天气可否改变",
                        "isKeepInv" to "物品不掉落",
                        "canFly" to "能否飞行",
                        "isBorder" to "启用世界边界",
                        "border" to "边界范围",
                        "isBreakRange" to "出生点范围内不允许破坏",
                        "unbreakableRange" to "出生点不可破坏范围",
                        "bannedItemList" to "%NONE%",
                        "whitelist" to "%NONE%",
                        "bannedCommandList" to "%NONE%"
                ))
                //configGUI.canChangeId = false
                wConfig.init()
                configGUI.init()
                player.showFormWindow(configGUI)

            }
        }
    }

}
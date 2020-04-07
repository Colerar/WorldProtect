package me.hbj233.worldprotect.module

import cn.nukkit.Player
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.item.Item
import me.hbj233.worldprotect.WorldProtectPlugin
import me.hbj233.worldprotect.gui.WorldProtectConfigGui
import me.hbj233.worldprotect.util.commandFormat
import top.wetabq.easyapi.command.EasyCommand
import top.wetabq.easyapi.command.EasySubCommand
import top.wetabq.easyapi.utils.color

object WorldProtectCommand : EasyCommand("wp", "World Protect") {

    private fun sendInputError(sender : Player) {
        sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,请检查输入是否正确.".color())
        sendHelp(sender)
    }

    fun sendNeedOp(sender: Player) = sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c权限不足.".color())

    init {

        val wConfig = WorldProtectModule.worldProtectConfig

        subCommand.add(object: EasySubCommand("gui") {
            override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
                if (sender is Player) {
                    if(sender.isOp) sender.showFormWindow(WorldProtectConfigGui()) else sendNeedOp(sender)
                }
                return true
            }

            override fun getAliases(): Array<String> = arrayOf("g", "ui")

            override fun getParameters(): Array<CommandParameter>? = null

            override fun getDescription(): String = "Open world protect gui"

        })

        subCommand.add(object : EasySubCommand("banitem"){
            override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
                if (sender is Player && sender.isOp && sender.isOnline){
                    val itemInHandId = sender.inventory?.itemInHand?.id
                    val worldBannedItemList= wConfig.safeGetData(sender.level.folderName).bannedItemList
                    when (args.size) {
                        2 -> when (args[1]) {//wp banitem add|remove
                            "add" -> {
                                if (itemInHandId != null && itemInHandId != 0) {
                                    worldBannedItemList.add(itemInHandId)
                                    sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你添加了手中的物品 ($itemInHandId) 到当前世界(${sender.level.folderName})的 ban 物品列表.".color())
                                } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,若你省略物品,则需要你手持物品.".color())
                            }
                            "remove" -> {
                                if (itemInHandId != null && itemInHandId != 0) {
                                    if (worldBannedItemList.contains(itemInHandId)) {
                                        worldBannedItemList.remove(itemInHandId)
                                        sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你把手中的物品 ($itemInHandId) 移出了当前世界的 ban 物品列表.".color())
                                    } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,该参数没有在 ban 物品列表中.".color())
                                } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,若你省略物品,则需要你手持物品.".color())
                            }
                        }
                        3 -> when (args[1]) {//wp banitem add|remove world
                            "add" -> {
                                if (itemInHandId != null && itemInHandId != 0) {
                                    if (wConfig.simpleConfig.containsKey(args[2])) {
                                        wConfig.safeGetData(args[2]).bannedItemList.add(itemInHandId)
                                        sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你添加了手中的物品 ($itemInHandId) 到世界 (${args[2]})的 ban 物品列表.".color())
                                    } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,世界名输入错误.".color())
                                } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,该模式下需要你手持物品.".color())
                            }
                            "remove" -> {
                                if (itemInHandId != null && itemInHandId != 0) {
                                    if (wConfig.simpleConfig.containsKey(args[2])) {
                                        if (wConfig.safeGetData(args[2]).bannedItemList.contains(itemInHandId)) {
                                            wConfig.safeGetData(args[2]).bannedItemList.remove(itemInHandId)
                                            sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你把手中的物品 ($itemInHandId) 移出了世界 (${args[2]})的 ban 物品列表.".color())
                                        } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,该参数没有在 ban 物品列表中.".color())
                                    } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,世界名输入错误.".color())
                                } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,若你省略物品,则需要你手持物品.".color())
                            }
                        }
                        4 -> when (args[1]) {//wp banitem add|remove world item
                            "add" -> {
                                if (WorldProtectModule.worldProtectConfig.simpleConfig.containsKey(args[2])) {
                                    if (Item.get(args[3].toInt()) != null && args[3].toInt() != 0) {
                                        WorldProtectModule.worldProtectConfig.safeGetData(args[2]).bannedItemList.add(args[3].toInt())
                                        sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你添加了手中的物品 (${args[3]} 到世界 (${args[2]})的 ban 物品列表.".color())
                                    } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,你的物品id输入错误.".color())
                                } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,世界名(${args[2]})输入错误.".color())
                            }
                            "remove" -> {
                                if (WorldProtectModule.worldProtectConfig.simpleConfig.containsKey(args[2])) {
                                    if (WorldProtectModule.worldProtectConfig.safeGetData(args[2]).bannedItemList.contains(args[3].toInt())) {
                                        WorldProtectModule.worldProtectConfig.safeGetData(args[2]).bannedItemList.remove(args[3].toInt())
                                        sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你把手中的物品 (${args[3]}) 移出了世界 (${args[2]})的 ban 物品列表.".color())
                                    } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,该参数没有在 ban 物品列表中.".color())
                                } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,世界名输入错误.".color())
                            }
                        }
                        else -> sendInputError(sender)
                    }
                    WorldProtectModule.worldProtectConfig.save()
                } else {
                    sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c权限不足.".color())
                }
                return true
            }

            override fun getAliases(): Array<String>? = arrayOf("banItem","bi")

            override fun getDescription(): String = "Change Banned Item list."

            override fun getParameters(): Array<CommandParameter>? =
                    arrayOf(CommandParameter("add|remove", arrayOf("add","remove")),
                        CommandParameter("world", CommandParamType.STRING,true),
                        CommandParameter("itemId", CommandParamType.INT,true))

        })

        subCommand.add(object : EasySubCommand("whitelist"){
            override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
                if (sender is Player) {
                    if (sender.isOp && sender.isOnline) {
                        when (args.size) {
                            2 ->{
                                val whitelist = wConfig.safeGetData(sender.level.folderName).whitelist
                                when (args[1]) {//wp whitelist add|remove
                                    "add" -> {
                                        whitelist.add(sender.name)
                                        sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你添加了自己 (${sender.name}) 到当前世界(${sender.level.folderName})的白名单.".color())
                                    }
                                    "remove" -> {
                                        if (whitelist.contains(sender.name)) {
                                            whitelist.remove(sender.name)
                                            sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你把你自己 (${sender.name}) 移出了当前世界的白名单.".color())
                                        } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,你 (${sender.name}) 没有在白名单中.".color())
                                    }
                                }
                            }
                            3 -> {
                                if (wConfig.simpleConfig.containsKey(args[2])) {
                                    when (args[1]) {//wp banitem add|remove world
                                        "add" -> {
                                            wConfig.safeGetData(args[2]).whitelist.add(sender.name)
                                            sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你添加了自己 (${sender.name}) 到世界 (${args[2]})的白名单.".color())
                                        }
                                        "remove" -> {
                                            if (wConfig.safeGetData(args[2]).whitelist.contains(sender.name)) {
                                                wConfig.safeGetData(args[2]).whitelist.remove(sender.name)
                                                sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你把你自己 (${sender.name}) 移出了世界 (${args[2]})的白名单.".color())
                                            } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,你没有在白名单中.".color())
                                        }
                                    }
                                } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,世界名输入错误.".color())
                            }
                            4 -> when (args[1]) {//wp banitem add|remove world item
                                "add" -> {
                                    if (wConfig.simpleConfig.containsKey(args[2])) {
                                        wConfig.safeGetData(args[2]).whitelist.add(args[3])
                                        sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你添加了玩家 (${args[3]} 到世界 (${args[2]})的白名单.".color())
                                    } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,世界名 (${args[2]}) 输入错误.".color())
                                }
                                "remove" -> {
                                    if (wConfig.simpleConfig.containsKey(args[2])) {
                                        if (wConfig.safeGetData(args[2]).whitelist.contains(args[3])) {
                                            wConfig.safeGetData(args[2]).whitelist.remove(args[3])
                                            sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你把玩家 (${args[3]}) 移出了世界 (${args[2]}) 的白名单.".color())
                                        } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,该玩家 (${args[3]}) 没有在白名单中.".color())
                                    } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,世界名输入错误.".color())
                                }
                            }
                            else -> sendInputError(sender)
                        }
                    } else sendInputError(sender)
                }
                return true
            }

            override fun getAliases(): Array<String>? = arrayOf("wl","w","white-list")

            override fun getDescription(): String = "Change whitelist."

            override fun getParameters(): Array<CommandParameter>? =
                    arrayOf(CommandParameter("add|remove", arrayOf("add","remove")),
                            CommandParameter("world", CommandParamType.STRING,true),
                            CommandParameter("Player", CommandParamType.TARGET,true))

        })

        subCommand.add(object : EasySubCommand("bancommand"){
            override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
                if (sender is Player) {
                    if (sender.isOp && sender.isOnline) when (args.size){
                        3 -> {
                            val bannedCommandList = wConfig.safeGetData(sender.level.folderName).bannedCommandList
                            when (args[1]){
                                "add" -> {
                                    if (!bannedCommandList.contains(args[2])){
                                        bannedCommandList.add(args[2].commandFormat())
                                        sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你于当前世界 (${sender.level.folderName}) 禁用了 (${args[2]}).".color())
                                    } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,该命令 (${args[2]}) 已存在于列表中.".color())
                                }
                                "remove" -> {
                                    if (bannedCommandList.contains(args[2])){
                                        bannedCommandList.remove(args[2].commandFormat())
                                        sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你解除了 (${args[2]}) 于当前世界 (${sender.level.folderName}) 的禁用状态.".color())
                                    } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,该命令 (${args[2]}) 不存在于列表中.".color())
                                }
                            }
                        }
                        4 -> {
                            if (wConfig.simpleConfig.containsKey(args[3])){
                                val bannedCommandList = wConfig.safeGetData(args[3]).bannedCommandList
                                when (args[1]) {
                                    "add" -> {
                                        if (!bannedCommandList.contains(args[2])){
                                            bannedCommandList.add(args[2].commandFormat())
                                            sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你限制了世界 (${args[3]})  ${args[2]} 指令的使用.".color())
                                        } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,该命令 (${args[2]}) 已存在于世界 (${args[3]}) 列表中.".color())
                                    }
                                    "remove" -> {
                                        if (bannedCommandList.contains(args[2])){
                                            bannedCommandList.remove(args[2].commandFormat())
                                            sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&e你解除限制了世界 (${args[3]}) ${args[2]} 指令的使用.".color())
                                        } else sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,该命令 (${args[2]}) 不存在于世界 (${args[3]}) 列表中.".color())
                                    }
                                }
                            } else {
                                sender.sendMessage("${WorldProtectPlugin.instance.TITLE}&c失败了,世界名输入错误.".color())
                            }
                        }
                        else -> sendInputError(sender)
                    }
                }
                return true
            }

            override fun getAliases(): Array<String>? = arrayOf("bc","banCommand")

            override fun getDescription(): String = "Change Banned Command List."

            override fun getParameters(): Array<CommandParameter>? = arrayOf(
                    CommandParameter("add|remove", arrayOf("add","remove")),
                    CommandParameter("command",CommandParamType.STRING,false),
                    CommandParameter("world",CommandParamType.STRING,true)
            )

        })

        loadCommandBase()
    }
}
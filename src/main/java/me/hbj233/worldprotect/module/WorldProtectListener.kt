package me.hbj233.worldprotect.module

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.block.*
import cn.nukkit.event.entity.*
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.event.level.WeatherChangeEvent
import cn.nukkit.event.player.*
import me.hbj233.worldprotect.WorldProtectPlugin
import me.hbj233.worldprotect.module.WorldProtectModule.worldProtectConfig
import me.hbj233.worldprotect.util.FormatMsgType
import me.hbj233.worldprotect.util.sendFormatMessage
import top.wetabq.easyapi.api.defaults.MessageFormatAPI
import top.wetabq.easyapi.utils.color

object WorldProtectListener : Listener {

    private fun sendAuthorityTips(player:Player) {
        // sendFormatMessage(player,"你无法进行此操作.",FormatMsgType.ERROR)
        sendFormatMessage(player, MessageFormatAPI.format(WorldProtectPlugin.instance.protectMessageFormat, player), FormatMsgType.TIP)
    }

    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        var isCancelled = false
        if (wConfig != null) {
            if (!wConfig.canInteract){
                isCancelled = !wConfig.whitelist.contains(event.player.name)
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onPlayerInteractEntityEvent(event: PlayerInteractEntityEvent) {
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canInteractEntity){
                isCancelled = !wConfig.whitelist.contains(event.player.name)
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onBlockPlaceEvent(event: BlockPlaceEvent){
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canPlace){
                isCancelled = !wConfig.whitelist.contains(event.player.name)
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onBlockBreakEvent(event: BlockBreakEvent){
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canBreak){
                isCancelled = !wConfig.whitelist.contains(event.player.name)
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onEntityVehicleEnterEvent(event: EntityVehicleEnterEvent){
        val p = event.entity
        if (p is Player) {
            var isCancelled = false
            val wConfig = worldProtectConfig.simpleConfig[p.level.folderName]
            if (wConfig != null) {
                if (!wConfig.canPlayerEnterVehicle){
                    isCancelled = !wConfig.whitelist.contains(p.name)
                }
            }
            if (isCancelled) sendAuthorityTips(p)
            event.isCancelled = isCancelled
        }
    }

    @EventHandler
    fun onPlayerGameModeChangeEvent(event: PlayerGameModeChangeEvent){
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canChangeGamemode){
                isCancelled = !wConfig.whitelist.contains(event.player.name)
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onEntityLevelChangeEvent(event: EntityLevelChangeEvent){
        val p = event.entity
        if (p is Player) {
            var isCancelled = false
            val wConfig = worldProtectConfig.simpleConfig[p.level.folderName]
            if (wConfig != null) {
                if (!wConfig.canChangeGamemode){
                    isCancelled = if (!wConfig.whitelist.contains(p.name)) false else {
                        p.setGamemode(wConfig.defaultGamemode)
                        true
                    }
                }
            }
            if (isCancelled) sendAuthorityTips(p)
            event.isCancelled = isCancelled
        }
    }

    @EventHandler
    fun onProjectileLaunchEvent(event: ProjectileLaunchEvent) {
        val player = event.entity.shootingEntity
        if (player is Player) {
            var isCancelled = false
            val wConfig = worldProtectConfig.simpleConfig[player.level.folderName]
            if (wConfig != null) {
                if (!wConfig.canProjLaunch){
                    isCancelled = !wConfig.whitelist.contains(player.name)
                }
            }
            if (isCancelled) sendAuthorityTips(player)
            event.isCancelled = isCancelled
        }
    }

    @EventHandler
    fun onPlayerDropItemEvent(event: PlayerDropItemEvent){
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canDropItem){
                isCancelled = !wConfig.whitelist.contains(event.player.name)
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onEntityDamageEvent(event: EntityDamageEvent){
        val player = event.entity
        if (player is Player){
            var isCancelled = false
            val wConfig = worldProtectConfig.simpleConfig[player.level.folderName]
            if (wConfig != null) {
                if (!wConfig.canBeDamaged){
                    isCancelled = true
                }
            }
            if (isCancelled) sendAuthorityTips(player)
            event.isCancelled = isCancelled
        }
    }

    @EventHandler
    fun onBlockBurnEvent(event: BlockBurnEvent){
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.block.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canBurn){
                isCancelled = true
            }
        }
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onBlockIgniteEvent(event: BlockIgniteEvent){
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.block.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canIgnite){
                isCancelled = !wConfig.whitelist.contains(event.entity.name)
            }
        }
        if (event.entity is Player && isCancelled) sendAuthorityTips(event.entity as Player)
        event.isCancelled = isCancelled
    }

    // Snow Layer
    @EventHandler
    fun onBlockFormEvent(event: BlockFormEvent){
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.block.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canBlockForm){
                isCancelled = true
            }
        }
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onLiquidFlowEvent(event: LiquidFlowEvent) {
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.block.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canLiquidFlow){
                isCancelled = true
            }
        }
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onLeavesDecayEvent(event: LeavesDecayEvent){
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.block.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canLeavesDecay){
                isCancelled = true
            }
        }
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onWeatherChangeEvent(event: WeatherChangeEvent){
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canWeatherChange) isCancelled = true
        }
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onPlayerItemHeldEvent(event: PlayerItemHeldEvent){
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) {
            if (wConfig.bannedItemList.contains(event.item.id)){
                isCancelled = true
                if(wConfig.whitelist.contains(event.player.name)){
                    isCancelled = false
                }
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onPlayerFoodLevelChangeEvent(event: PlayerFoodLevelChangeEvent) {
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) if (!wConfig.canFoodLevelChange){
            isCancelled = true
            //event.player?.foodData?.setLevel(20,5F)
        }
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onEntityExplodeEvent(event: EntityExplodeEvent) {
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.entity.level.folderName]
        if (wConfig != null) if (!wConfig.canExplosion){
            isCancelled = true
        }
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onInventoryTransactionEvent(event: InventoryTransactionEvent) {
        var isCancelled = false
        val wConfig = worldProtectConfig.simpleConfig[event.transaction.source.level.folderName]
        if (wConfig != null) if (!wConfig.canInventoryTransaction){
            isCancelled = true
            if (wConfig.whitelist.contains(event.transaction.source.name)){
                isCancelled = false
            }
        }
        event.isCancelled = isCancelled
    }

    @EventHandler
    fun onPlayerCommandPreprocessEvent(event: PlayerCommandPreprocessEvent){
        var isCancelled = false

        //println(event.message.commandFormat())
        var regex1 : Regex

        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig?.whitelist?.contains(event.player.name)?.not() != false) {
            wConfig?.bannedCommandList?.forEach { s ->
                regex1 = s.toRegex()
                if(regex1.matches(event.message)){
                    event.player.sendMessage("${WorldProtectPlugin.instance.TITLE}&c当前世界 (${event.player.level.folderName}) 禁用了该命令 (${event.message}).".color())
                    isCancelled = true
                }
            }
        }

        event.isCancelled = isCancelled
    }
}
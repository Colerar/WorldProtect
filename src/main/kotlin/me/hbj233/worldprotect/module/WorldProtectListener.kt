package me.hbj233.worldprotect.module

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.block.*
import cn.nukkit.event.entity.*
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.event.level.WeatherChangeEvent
import cn.nukkit.event.player.*
import me.hbj233.worldprotect.WorldProtectPlugin
import me.hbj233.worldprotect.module.WorldProtectModule.worldProtectConfig
import me.hbj233.worldprotect.util.FormatMsgType
import me.hbj233.worldprotect.util.isInRange
import me.hbj233.worldprotect.util.sendFormatMessage
import top.wetabq.easyapi.api.defaults.MessageFormatAPI
import top.wetabq.easyapi.utils.color


object WorldProtectListener : Listener {

    internal fun sendAuthorityTips(player: Player) {
        // sendFormatMessage(player,"你无法进行此操作.",FormatMsgType.ERROR)
        sendFormatMessage(player, MessageFormatAPI.format(WorldProtectPlugin.instance.protectMessageFormat, player), FormatMsgType.TIP)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        var isCancelled = event.isCancelled
        if (wConfig != null) {
            if (!wConfig.canInteract){
                isCancelled = !wConfig.whitelist.contains(event.player.name)
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerInteractEntityEvent(event: PlayerInteractEntityEvent) {
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canInteractEntity){
                isCancelled = !wConfig.whitelist.contains(event.player.name)
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onBlockPlaceEvent(event: BlockPlaceEvent){
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) {
            if (!wConfig.whitelist.contains(event.player.name)) {
                if (!wConfig.canPlace) {
                    isCancelled = true
                }
                if (wConfig.isBreakPutRange) {
                    if (event.block.location.isInRange(event.player.level.spawnLocation.location, wConfig.unbreakPutRange)) {
                        isCancelled = true
                    }
                }
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onBlockBreakEvent(event: BlockBreakEvent){
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) {
            if (!wConfig.whitelist.contains(event.player.name)) {
                if (!wConfig.canBreak) {
                    isCancelled = true
                }
                val spawnPoint = event.player.level.spawnLocation
                if (wConfig.isBreakPutRange) {
                    if (isInRange(event.block.x, event.block.z, spawnPoint.x, spawnPoint.z, wConfig.unbreakPutRange)) {
                        isCancelled = true
                    }
                }
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityVehicleEnterEvent(event: EntityVehicleEnterEvent){
        val p = event.entity
        if (p is Player) {
            var isCancelled = event.isCancelled
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

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerGameModeChangeEvent(event: PlayerGameModeChangeEvent){
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canChangeGamemode){
                isCancelled = !wConfig.whitelist.contains(event.player.name)
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityLevelChangeEvent(event: EntityLevelChangeEvent){
        val p = event.entity
        if (p is Player) {
            val wConfig = worldProtectConfig.simpleConfig[event.target.folderName]
            if (wConfig != null) {
                if (!wConfig.canChangeGamemode) {
                    if (!wConfig.whitelist.contains(p.name)) {
                        p.setGamemode(wConfig.defaultGamemode)
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityTeleportEvent(event: EntityTeleportEvent) {
        val p = event.entity
        if (p is Player) {
            val wConfig = worldProtectConfig.simpleConfig[event.to.level.folderName]
            if (wConfig != null) {
                if (!wConfig.canChangeGamemode) {
                    if (!wConfig.whitelist.contains(p.name)) {
                        p.setGamemode(wConfig.defaultGamemode)
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onProjectileLaunchEvent(event: ProjectileLaunchEvent) {
        val player = event.entity.shootingEntity
        if (player is Player) {
            var isCancelled = event.isCancelled
            val wConfig = worldProtectConfig.simpleConfig[player.level.folderName]
            if (wConfig != null) {
                if (!wConfig.canProjLaunch) {
                    isCancelled = !wConfig.whitelist.contains(player.name)
                }
            }
            if (isCancelled) sendAuthorityTips(player)
            event.isCancelled = isCancelled
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerDropItemEvent(event: PlayerDropItemEvent){
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canDropItem){
                isCancelled = !wConfig.whitelist.contains(event.player.name)
            }
        }
        if (isCancelled) sendAuthorityTips(event.player)
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityDamageEvent(event: EntityDamageEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            val player = event.entity
            if (player is Player) {
                var isCancelled = event.isCancelled
                val wConfig = worldProtectConfig.simpleConfig[player.level.folderName]
                if (wConfig != null) {
                    if (!wConfig.canBeDamaged) {
                        isCancelled = true
                    }
                }
                if (isCancelled) event.damage = 0.0F
                event.isCancelled = isCancelled
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityDamageByEntityEvent(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val damager = event.damager

        val wConfig = worldProtectConfig.safeGetData(entity.level.folderName)
        if (!wConfig.canBeDamaged) {
            var damage = 0.0F
            var isCancelled = true
            if (damager is Player) {
                if (!wConfig.whitelist.contains(event.damager.name)) {
                    if (entity != damager) {
                        sendAuthorityTips(damager)
                        damage = 0.0F
                        isCancelled = true
                    }
                }
            }
            event.damage = damage
            event.isCancelled = isCancelled
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onBlockBurnEvent(event: BlockBurnEvent) {
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.block.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canBurn) {
                isCancelled = true
            }
        }
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onBlockIgniteEvent(event: BlockIgniteEvent){
        var isCancelled = event.isCancelled
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
    @EventHandler(priority = EventPriority.LOWEST)
    fun onBlockFormEvent(event: BlockFormEvent){
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.block.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canBlockForm){
                isCancelled = true
            }
        }
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onLiquidFlowEvent(event: LiquidFlowEvent) {
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.block.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canLiquidFlow){
                isCancelled = true
            }
        }
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onLeavesDecayEvent(event: LeavesDecayEvent){
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.block.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canLeavesDecay){
                isCancelled = true
            }
        }
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onWeatherChangeEvent(event: WeatherChangeEvent){
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.level.folderName]
        if (wConfig != null) {
            if (!wConfig.canWeatherChange) isCancelled = true
        }
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerItemHeldEvent(event: PlayerItemHeldEvent){
        var isCancelled = event.isCancelled
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

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerFoodLevelChangeEvent(event: PlayerFoodLevelChangeEvent) {
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.player.level.folderName]
        if (wConfig != null) if (!wConfig.canFoodLevelChange){
            isCancelled = true
            //event.player?.foodData?.setLevel(20,5F)
        }
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityExplodeEvent(event: EntityExplodeEvent) {
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.entity.level.folderName]
        if (wConfig != null) if (!wConfig.canExplosion){
            isCancelled = true
        }
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryTransactionEvent(event: InventoryTransactionEvent) {
        var isCancelled = event.isCancelled
        val wConfig = worldProtectConfig.simpleConfig[event.transaction.source.level.folderName]
        if (wConfig != null) if (!wConfig.canInventoryTransaction) {
            isCancelled = true
            if (wConfig.whitelist.contains(event.transaction.source.name)) {
                isCancelled = false
            }
        }
        event.isCancelled = isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerDie(event: PlayerDeathEvent) {
        if (event.entity is Player) {
            val wConfig = worldProtectConfig.simpleConfig[event.entity.level.folderName]
            if (wConfig != null) if (wConfig.isKeepInv) {
                event.keepExperience = true
                event.keepInventory = true
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerInvalidMove(event: PlayerInvalidMoveEvent) {
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerCommandPreprocessEvent(event: PlayerCommandPreprocessEvent) {
        var isCancelled = event.isCancelled
        //println(event.message.commandFormat())
        var regex1: Regex

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
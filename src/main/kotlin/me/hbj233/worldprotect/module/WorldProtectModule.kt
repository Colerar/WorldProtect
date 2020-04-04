package me.hbj233.worldprotect.module

import cn.nukkit.Player
import cn.nukkit.level.particle.RedstoneParticle
import cn.nukkit.math.Vector2
import cn.nukkit.math.Vector3
import me.hbj233.worldprotect.WorldProtectPlugin
import me.hbj233.worldprotect.data.WorldProtectData
import me.hbj233.worldprotect.util.ParticleUtils
import me.hbj233.worldprotect.util.isInRange
import top.wetabq.easyapi.api.defaults.*
import top.wetabq.easyapi.config.defaults.SimpleConfigEntry
import top.wetabq.easyapi.config.encoder.advance.SimpleCodecEasyConfig
import top.wetabq.easyapi.module.ModuleInfo
import top.wetabq.easyapi.module.ModuleVersion
import top.wetabq.easyapi.module.SimpleEasyAPIModule
import kotlin.math.tanh

object WorldProtectModule : SimpleEasyAPIModule() {

    private const val MODULE_NAME = "WorldProtect"
    private const val AUTHOR = "HBJ233"

    const val SIMPLE_CONFIG = "worldProtectSimpleConfig"
    const val WORLD_CONFIG = "worldProtectConfig"
    const val WORLD_PROTECT_LISTENER = "worldProtectListener"

    lateinit var worldProtectConfig: SimpleCodecEasyConfig<WorldProtectData>

    private var pullStrength = 1.0

    override fun getModuleInfo(): ModuleInfo = ModuleInfo(
            WorldProtectPlugin.instance,
            MODULE_NAME,
            AUTHOR,
            ModuleVersion(1, 2, 0)
    )

    override fun moduleRegister() {

        val simpleConfig = this.registerAPI(SIMPLE_CONFIG, SimpleConfigAPI(WorldProtectPlugin.instance))
                .add(SimpleConfigEntry(".title", WorldProtectPlugin.instance.TITLE))
                .add(SimpleConfigEntry(".protect.message", WorldProtectPlugin.instance.protectMessageFormat))
                .add(SimpleConfigEntry(".protect.pullstrength", pullStrength))

        WorldProtectPlugin.instance.TITLE = simpleConfig.getPathValue(".title") as String?
                ?: WorldProtectPlugin.instance.TITLE
        WorldProtectPlugin.instance.protectMessageFormat = simpleConfig.getPathValue(".protect.message") as String?
                ?: WorldProtectPlugin.instance.protectMessageFormat
        pullStrength = (simpleConfig.getPathValue(".protect.pullstrength") as String?)?.toDouble() ?: 1.0

        MessageFormatAPI.registerSimpleFormatter(object : SimpleMessageFormatter {
            override fun format(message: String): String = message.replace("%world_protect_title%", WorldProtectPlugin.instance.TITLE)
        })

        worldProtectConfig = object : SimpleCodecEasyConfig<WorldProtectData>(
                "worldProtect", WorldProtectPlugin.instance, WorldProtectData::class.java,
                WorldProtectData(canInteract = true, canInteractEntity = true,
                        canPlayerEnterVehicle = true, canChangeGamemode = true, defaultGamemode = 1, canProjLaunch = true, canDropItem = true, canFoodLevelChange = true,
                        canPlace = true, canBreak = true, canBurn = true, canIgnite = true, canBlockForm = true, canLiquidFlow = true,
                        canExplosion = true, canLeavesDecay = true, canWeatherChange = true, canBeDamaged = true, canInventoryTransaction = true,
                        bannedItemList = mutableListOf(), whitelist = mutableListOf(), bannedCommandList = mutableListOf(), isKeepInv = false,
                        canFly = true, isBorder = false, border = 50, isBreakPutRange = false, unbreakPutRange = 50)
        ) {}
        worldProtectConfig.init()

        WorldProtectPlugin.instance.server.levels.values.forEach { level ->
            val config = worldProtectConfig.simpleConfig
            if (!config.containsKey(level.folderName)) {
                config[level.folderName] = worldProtectConfig.getDefaultValue()
                worldProtectConfig.save()
            }
        }

        SimplePluginTaskAPI.repeating(20) { _, _ ->

            WorldProtectPlugin.instance.server.levels.values
                    .fold(mutableListOf<String>(), { acc, level -> acc.add(level.folderName); acc })
                    .forEach { it ->
                        val wConfig = worldProtectConfig.safeGetData(it)
                        if (wConfig.isBorder) {
                            WorldProtectPlugin.instance.server.levels.values
                                    .filter { it2 -> it2.folderName == it }
                                    .fold(mutableListOf<Player>(), { acc, level -> acc.addAll(level.players.values); acc })
                                    .forEach {
                                        if (!wConfig.whitelist.contains(it.name)) {
                                            val spawnPoint = it.level.spawnLocation
                                            if ((!it.position.isInRange(spawnPoint, wConfig.border))) {
                                                val d1: Int = it.directionPlane.round().floorX
                                                val d2: Int = it.directionPlane.round().floorY
                                                if (d1 != 0) {
                                                    ParticleUtils.rectangle(it.add(0.0, 5.0, 5.0), it.subtract(0.0, 5.0, 5.0), 0.5, RedstoneParticle(Vector3()), listOf(it))
                                                } else if (d2 != 0) {
                                                    ParticleUtils.rectangle(it.add(5.0, 5.0, 0.0), it.subtract(0.0, 5.0, 0.0), 0.5, RedstoneParticle(Vector3()), listOf(it))
                                                }
                                                WorldProtectListener.sendAuthorityTips(it)
                                                val playerVectorToSpawn = Vector2(it.level.spawnLocation.x - it.x, it.level.spawnLocation.z - it.z)
                                                it.setMotion(Vector3(tanh(playerVectorToSpawn.x) * pullStrength, 1.5, tanh(playerVectorToSpawn.y) * pullStrength))
                                            }
                                        }
                                    }
                        }
                    }

        }

        this.registerAPI(WORLD_CONFIG, ConfigAPI())
                .add(worldProtectConfig)

        this.registerAPI(WORLD_PROTECT_LISTENER, NukkitListenerAPI(WorldProtectPlugin.instance))
                .add(WorldProtectListener)

        this.registerAPI("worldProtectCommand", CommandAPI())
                .add(WorldProtectCommand)
    }

    override fun moduleDisable() {
    }

}
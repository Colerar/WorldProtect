package me.hbj233.worldprotect.module

import me.hbj233.worldprotect.WorldProtectPlugin
import me.hbj233.worldprotect.data.WorldProtectData
import top.wetabq.easyapi.api.defaults.*
import top.wetabq.easyapi.config.defaults.SimpleConfigEntry
import top.wetabq.easyapi.config.encoder.advance.SimpleCodecEasyConfig
import top.wetabq.easyapi.module.ModuleInfo
import top.wetabq.easyapi.module.ModuleVersion
import top.wetabq.easyapi.module.SimpleEasyAPIModule

object WorldProtectModule : SimpleEasyAPIModule() {

    private const val MODULE_NAME = "WorldProtect"
    private const val AUTHOR = "HBJ233"

    const val SIMPLE_CONFIG = "worldProtectSimpleConfig"
    const val WORLD_CONFIG = "worldProtectConfig"
    const val WORLD_PROTECT_LISTENER = "worldProtectListener"

    lateinit var worldProtectConfig: SimpleCodecEasyConfig<WorldProtectData>

    override fun getModuleInfo(): ModuleInfo = ModuleInfo(
            WorldProtectPlugin.instance,
            MODULE_NAME,
            AUTHOR,
            ModuleVersion(1, 1, 0)
    )

    override fun moduleRegister() {

        val simpleConfig = this.registerAPI(SIMPLE_CONFIG, SimpleConfigAPI(WorldProtectPlugin.instance))
                .add(SimpleConfigEntry("title", WorldProtectPlugin.instance.TITLE))
                .add(SimpleConfigEntry("protect.message", WorldProtectPlugin.instance.protectMessageFormat))

        WorldProtectPlugin.instance.TITLE = simpleConfig.getPathValue("title") as String? ?: WorldProtectPlugin.instance.TITLE
        WorldProtectPlugin.instance.protectMessageFormat = simpleConfig.getPathValue("protect.message") as String? ?: WorldProtectPlugin.instance.protectMessageFormat

        MessageFormatAPI.registerSimpleFormatter(object: SimpleMessageFormatter {
            override fun format(message: String): String = message.replace("%world_protect_title%", WorldProtectPlugin.instance.TITLE)
        })

        worldProtectConfig = object : SimpleCodecEasyConfig<WorldProtectData>(
                "worldProtect", WorldProtectPlugin.instance, WorldProtectData::class.java,
                WorldProtectData(canInteract = true, canInteractEntity = true,
                canPlayerEnterVehicle = true, canChangeGamemode = true, defaultGamemode = 1, canProjLaunch = true, canDropItem = true, canFoodLevelChange = true,
                canPlace = true, canBreak = true, canBurn = true, canIgnite = true,canBlockForm = true, canLiquidFlow = true,
                canExplosion = true, canLeavesDecay = true,canWeatherChange = true, canBeDamaged = true, canInventoryTransaction = true,
                bannedItemList = mutableListOf(), whitelist = mutableListOf(), bannedCommandList = mutableListOf())
        ) {}
        worldProtectConfig.init()

        WorldProtectPlugin.instance.server.levels.values.forEach { level ->
            val config = worldProtectConfig.simpleConfig
            if (!config.containsKey(level.folderName)) {
                config[level.folderName] = worldProtectConfig.getDefaultValue()
                worldProtectConfig.save()
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
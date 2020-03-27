package me.hbj233.worldprotect

import cn.nukkit.plugin.PluginBase
import me.hbj233.worldprotect.module.WorldProtectModule
import top.wetabq.easyapi.module.EasyAPIModuleManager
import top.wetabq.easyapi.utils.color

class WorldProtectPlugin : PluginBase() {

    var TITLE = "&e[&aWorldProtect&e]&r".color()
    var protectMessageFormat = "%world_protect_title% &c&l你无法进行此操作."

    override fun onEnable() {
        instance = this
        EasyAPIModuleManager.register(WorldProtectModule)
    }

    companion object{
        lateinit var instance : WorldProtectPlugin
    }
}
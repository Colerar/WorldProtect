package me.hbj233.worldprotect.util

import cn.nukkit.Player
import me.hbj233.worldprotect.WorldProtectPlugin
import top.wetabq.easyapi.module.defaults.ScreenShowModule
import top.wetabq.easyapi.screen.ScreenShow
import top.wetabq.easyapi.screen.ShowType
import top.wetabq.easyapi.utils.color

enum class FormatMsgType {
    TIP, ERROR
}

fun sendFormatMessage(p: Player, message :String,
                      formatMsgType : FormatMsgType = FormatMsgType.TIP) {
    if (p.isOnline) when (formatMsgType) {
        FormatMsgType.TIP -> ScreenShowModule.addScreenShow(ScreenShow(setOf(p), message.color(), ScreenShowModule.HIGHEST_PRIORITY, 20, 20, false, false, ShowType.TIP))
        FormatMsgType.ERROR -> ScreenShowModule.addScreenShow(ScreenShow(setOf(p), message.color(), ScreenShowModule.HIGHEST_PRIORITY, 20, 20, false, false, ShowType.TIP))
    } else {
        WorldProtectPlugin.instance.logger.warning("给玩家 $p 发送消息时出现错误，原因：其未在线。")
    }
}
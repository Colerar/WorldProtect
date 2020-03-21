package me.hbj233.worldprotect.data

data class WorldProtectData(
        val canInteract: Boolean,
        val canInteractEntity:Boolean,
        val canPlayerEnterVehicle: Boolean,
        val canChangeGamemode: Boolean,
        val defaultGamemode: Int,
        val canProjLaunch: Boolean,
        val canDropItem: Boolean,
        val canBeDamaged: Boolean,

        val canFoodLevelChange: Boolean,
        val canExplosion: Boolean,
        val canInventoryTransaction: Boolean,

        val canPlace: Boolean,
        val canBreak: Boolean,
        val canBurn: Boolean,
        val canIgnite : Boolean,
        val canBlockForm: Boolean,
        val canLiquidFlow: Boolean,
        val canLeavesDecay: Boolean,
        val canWeatherChange: Boolean,

        var bannedItemList: MutableList<Int>,
        var whitelist: MutableList<String>,
        var bannedCommandList: MutableList<String>
)
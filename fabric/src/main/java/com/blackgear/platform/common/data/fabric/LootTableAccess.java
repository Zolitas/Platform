package com.blackgear.platform.common.data.fabric;

import net.minecraft.world.level.storage.loot.LootPool;

import java.util.List;

public interface LootTableAccess {
    List<LootPool> platform$getPools();
    
    void platform$setPools(List<LootPool> pools);
}
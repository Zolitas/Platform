package com.blackgear.platform.common.data.forge;

import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

public interface LootPoolAccess {
    LootPoolEntryContainer[] platform$getEntries();
    
    void platform$setEntries(LootPoolEntryContainer[] entries);
}
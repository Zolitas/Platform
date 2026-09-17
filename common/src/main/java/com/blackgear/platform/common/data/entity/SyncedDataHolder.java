package com.blackgear.platform.common.data.entity;

import net.minecraft.world.entity.Entity;

public interface SyncedDataHolder {
    static SyncedEntityDataContainer get(Entity entity) {
        return ((SyncedDataHolder) entity).getPlatformData();
    }
    
    SyncedEntityDataContainer getPlatformData();
}
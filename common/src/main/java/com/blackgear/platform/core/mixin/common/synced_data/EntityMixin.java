package com.blackgear.platform.core.mixin.common.synced_data;

import com.blackgear.platform.common.data.entity.SyncedDataHolder;
import com.blackgear.platform.common.data.entity.SyncedEntityDataContainer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class EntityMixin implements SyncedDataHolder {
    @Unique private final SyncedEntityDataContainer platform$data = new SyncedEntityDataContainer((Entity) (Object) this);
    
    @Override
    public SyncedEntityDataContainer getPlatformData() {
        return this.platform$data;
    }
}
package com.blackgear.platform.core.mixin.common.attributes;

import com.blackgear.platform.common.v2.entity.impl.AttributeUpdater;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(AttributeMap.class)
public class AttributeInstanceMixin implements AttributeUpdater {
    @Unique private final Set<AttributeInstance> platform$attributesToUpdate = new ObjectOpenHashSet<>();
    
    @Inject(method = "onAttributeModified", at = @At("HEAD"))
    private void platform$onAttributeModified(AttributeInstance instance, CallbackInfo ci) {
        this.platform$attributesToUpdate.add(instance);
    }
    
    @Override
    public Set<AttributeInstance> platform$getAttributesToUpdate() {
        return this.platform$attributesToUpdate;
    }
}
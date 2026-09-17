package com.blackgear.platform.core.mixin.common.attributes;

import com.blackgear.platform.common.v2.entity.EntityAttributeEvents;
import com.blackgear.platform.common.v2.entity.impl.AttributeUpdater;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract AttributeMap getAttributes();
    
    @Inject(method = "onEffectUpdated", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;addAttributeModifiers(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/AttributeMap;I)V"))
    private void platform$onEffectUpdated(MobEffectInstance effect, boolean forced, Entity entity, CallbackInfo ci) {
        this.platform$refreshDirtyAttributes();
    }
    
    @Inject(method = "onEffectRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/AttributeMap;I)V"))
    private void platform$onEffectRemoved(MobEffectInstance effect, CallbackInfo ci) {
        this.platform$refreshDirtyAttributes();
    }
    
    @Inject(method = "tick", at = @At("TAIL"))
    private void platform$onTick(CallbackInfo ci) {
        this.platform$refreshDirtyAttributes();
    }
    
    @Unique
    private void platform$refreshDirtyAttributes() {
        Set<AttributeInstance> attributes = ((AttributeUpdater) this.getAttributes()).platform$getAttributesToUpdate();
        
        for (AttributeInstance instance : attributes) {
            this.platform$onAttributeUpdated(instance.getAttribute());
        }
        
        attributes.clear();
    }
    
    @Unique
    private void platform$onAttributeUpdated(Attribute attribute) {
        EntityAttributeEvents.ON_LIVING_ATTRIBUTE_UPDATE.invoker().onUpdate((LivingEntity) (Object) this, attribute);
    }
}
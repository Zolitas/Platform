package com.blackgear.platform.core.mixin.common.attributes;

import com.blackgear.platform.common.v2.entity.EntityAttributeEvents;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "onAttributeUpdated", at = @At("TAIL"))
    private void platform$onAttributeUpdated(Holder<Attribute> attribute, CallbackInfo ci) {
        EntityAttributeEvents.ON_LIVING_ATTRIBUTE_UPDATE.invoker().onUpdate((LivingEntity) (Object) this, attribute);
    }
}
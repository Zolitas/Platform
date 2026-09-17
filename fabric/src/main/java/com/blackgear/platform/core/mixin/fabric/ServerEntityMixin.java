package com.blackgear.platform.core.mixin.fabric;

import com.blackgear.platform.common.events.EntityTrackingEvents;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {
    @Shadow @Final private Entity entity;
    
    @Inject(method = "addPairing", at = @At("TAIL"))
    private void platform$startTrackingEntity(ServerPlayer player, CallbackInfo ci) {
        EntityTrackingEvents.START_TRACKING.invoker().onTracking(this.entity, player);
    }
    
    @Inject(method = "removePairing", at = @At("TAIL"))
    private void platform$stopTrackingEntity(ServerPlayer player, CallbackInfo ci) {
        EntityTrackingEvents.STOP_TRACKING.invoker().onTracking(this.entity, player);
    }
}
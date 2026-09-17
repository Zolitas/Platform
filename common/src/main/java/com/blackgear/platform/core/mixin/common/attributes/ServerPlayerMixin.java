package com.blackgear.platform.core.mixin.common.attributes;

import com.blackgear.platform.common.v2.entity.EntityAttributeEvents;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;trackEnteredOrExitedLavaOnVehicle()V"))
    private void platform$onTick(CallbackInfo ci) {
        this.platform$updatePlayerAttributes();
    }
    
    @Unique
    private void platform$updatePlayerAttributes() {
        EntityAttributeEvents.UPDATE_PLAYER_ATTRIBUTE.invoker().update((ServerPlayer) (Object) this);
    }
}
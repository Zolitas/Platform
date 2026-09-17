package com.blackgear.platform.core.mixin.client.hud;

import com.blackgear.platform.client.event.screen.hud.HudElementRegistryImpl;
import com.blackgear.platform.client.event.screen.hud.VanillaHudElements;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BossHealthOverlay.class)
public abstract class BossHealthOverlayMixin {
    @Shadow @Final private Minecraft minecraft;
    
    @WrapMethod(method = "render")
    private void platform$bossHealthLayer(GuiGraphics guiGraphics, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.BOSS_BAR).render(guiGraphics, this.minecraft.getTimer(), (ctx, tc) -> original.call(ctx));
    }
}
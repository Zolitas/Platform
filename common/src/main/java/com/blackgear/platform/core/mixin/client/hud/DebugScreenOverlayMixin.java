package com.blackgear.platform.core.mixin.client.hud;

import com.blackgear.platform.client.event.screen.hud.HudElementRegistryImpl;
import com.blackgear.platform.client.event.screen.hud.VanillaHudElements;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DebugScreenOverlay.class)
public abstract class DebugScreenOverlayMixin {
    @Shadow @Final private Minecraft minecraft;
    
    @WrapMethod(method = "render")
    private void platform$debugScreenLayer(GuiGraphics guiGraphics, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.DEBUG).render(guiGraphics, this.minecraft.getTimer(), (ctx, tc) -> original.call(ctx));
    }
}
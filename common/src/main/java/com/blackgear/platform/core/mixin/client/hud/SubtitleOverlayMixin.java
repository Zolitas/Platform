package com.blackgear.platform.core.mixin.client.hud;

import com.blackgear.platform.client.event.screen.hud.HudElementRegistryImpl;
import com.blackgear.platform.client.event.screen.hud.VanillaHudElements;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SubtitleOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SubtitleOverlay.class)
public abstract class SubtitleOverlayMixin {
    @Shadow @Final private Minecraft minecraft;
    
    @WrapMethod(method = "render")
    private void platform$subtitleOverlayLayer(GuiGraphics guiGraphics, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.SUBTITLES).render(guiGraphics, this.minecraft.getTimer(), (ctx, tc) -> original.call(ctx));
    }
}
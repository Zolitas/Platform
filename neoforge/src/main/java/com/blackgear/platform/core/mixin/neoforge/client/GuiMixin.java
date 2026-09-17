package com.blackgear.platform.core.mixin.neoforge.client;

import com.blackgear.platform.client.event.screen.hud.HudElementRegistryImpl;
import com.blackgear.platform.client.event.screen.hud.VanillaHudElements;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow @Final private Minecraft minecraft;
    
    @WrapMethod(method = "renderCameraOverlays")
    private void platform$overlayLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.MISC_OVERLAYS).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapMethod(method = "renderCrosshair")
    private void platform$crosshairLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.CROSSHAIR).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapMethod(method = "renderExperienceLevel")
    private void platform$experienceLevelLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.EXPERIENCE_LEVEL).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapMethod(method = "renderEffects")
    private void platform$statusEffectLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.STATUS_EFFECTS).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapMethod(method = "renderDemoOverlay")
    private void platform$demoTimerLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.DEMO_TIMER).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapMethod(method = "renderScoreboardSidebar")
    private void platform$scoreboardLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.SCOREBOARD).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapMethod(method = "renderOverlayMessage")
    private void platform$overlayMessageLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.OVERLAY_MESSAGE).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapMethod(method = "renderTitle")
    private void platform$titleAndSubtitleLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.TITLE_AND_SUBTITLE).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapMethod(method = "renderChat")
    private void platform$chatLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.CHAT).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapMethod(method = "renderTabList")
    private void platform$playerListLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.PLAYER_LIST).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapMethod(method = "renderSleepOverlay")
    private void platform$sleepLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.SLEEP).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapOperation(method = "renderHotbar",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/spectator/SpectatorGui;renderHotbar(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void platform$spectatorMenuLayer(SpectatorGui instance, GuiGraphics guiGraphics, Operation<Void> original, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.SPECTATOR_MENU).render(guiGraphics, deltaTracker, (ctx, tc) -> original.call(instance, ctx));
    }
    
    @WrapOperation(method = "renderHotbar",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderItemHotbar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"))
    private void platform$hotbarLayer(Gui instance, GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.HOTBAR).render(guiGraphics, deltaTracker, (ctx, tc) -> original.call(instance, ctx, tc));
    }
    
    @WrapMethod(method = "maybeRenderJumpMeter")
    private void platform$jumpMeterLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.INFO_BAR).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapMethod(method = "maybeRenderExperienceBar")
    private void platform$experienceBarLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.INFO_BAR).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapOperation(method = "maybeRenderVehicleHealth",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderVehicleHealth(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void platform$mountHealthLayer(Gui instance, GuiGraphics guiGraphics, Operation<Void> original, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.MOUNT_HEALTH).render(guiGraphics, deltaTracker, (ctx, tc) -> original.call(instance, ctx));
    }
    
    @WrapOperation(method = "maybeRenderSelectedItemName",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V"))
    private void platform$itemTooltipLayer(Gui instance, GuiGraphics guiGraphics, int yShift, Operation<Void> original, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.HELD_ITEM_TOOLTIP).render(guiGraphics, deltaTracker, (ctx, tc) -> original.call(instance, ctx, yShift));
    }
    
    @WrapOperation(method = "maybeRenderSpectatorTooltip",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/spectator/SpectatorGui;renderTooltip(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void platform$spectatorTooltipLayer(SpectatorGui instance, GuiGraphics guiGraphics, Operation<Void> original, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.SPECTATOR_TOOLTIP).render(guiGraphics, deltaTracker, (ctx, tc) -> original.call(instance, ctx));
    }
    
    @WrapMethod(method = "renderHealthLevel")
    private void platform$healthBarLayer(GuiGraphics guiGraphics, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.HEALTH_BAR).render(guiGraphics, this.minecraft.getTimer(), (ctx, tc) -> original.call(ctx));
    }
    
    @WrapMethod(method = "renderArmorLevel")
    private void platform$armorBarLayer(GuiGraphics guiGraphics, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.ARMOR_BAR).render(guiGraphics, this.minecraft.getTimer(), (ctx, tc) -> original.call(ctx));
    }
    
    @WrapMethod(method = "renderFoodLevel")
    private void platform$foodBarLayer(GuiGraphics guiGraphics, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.FOOD_BAR).render(guiGraphics, this.minecraft.getTimer(), (ctx, tc) -> original.call(ctx));
    }
    
    @WrapMethod(method = "renderAirLevel")
    private void platform$airBarLayer(GuiGraphics guiGraphics, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.AIR_BAR).render(guiGraphics, this.minecraft.getTimer(), (ctx, tc) -> original.call(ctx));
    }
}
package com.blackgear.platform.core.mixin.fabric.client;

import com.blackgear.platform.client.event.HudRenderEvent;
import com.blackgear.platform.client.event.screen.hud.HudElementRegistryImpl;
import com.blackgear.platform.client.event.screen.hud.VanillaHudElements;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow protected abstract void renderExperienceBar(GuiGraphics guiGraphics, int x);
    
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDraw;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"))
    private void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        HudRenderEvent.RenderContext context = new HudRenderEvent.RenderContext() {
            @Override public Window window() { return minecraft.getWindow(); }
            @Override public int screenWidth() { return guiGraphics.guiWidth(); }
            @Override public int screenHeight() { return guiGraphics.guiHeight(); }
        };
        HudRenderEvent.RENDER_HUD.invoker().render(guiGraphics, deltaTracker.getGameTimeDeltaTicks(), HudRenderEvent.ElementType.FIRST_PERSON, context);
    }
    
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
    private void platform$SleepLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.SLEEP).render(guiGraphics, deltaTracker, original::call);
    }
    
    @WrapOperation(method = "renderHotbarAndDecorations",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/spectator/SpectatorGui;renderHotbar(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void platform$spectatorMenuLayer(SpectatorGui instance, GuiGraphics guiGraphics, Operation<Void> original, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.SPECTATOR_MENU).render(guiGraphics, deltaTracker, (ctx, tc) -> original.call(instance, ctx));
    }
    
    @WrapOperation(method = "renderHotbarAndDecorations",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderItemHotbar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"))
    private void platform$hotbarLayer(Gui instance, GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.HOTBAR).render(guiGraphics, deltaTracker, (ctx, tc) -> original.call(instance, ctx, tc));
    }
    
    @WrapOperation(method = "renderHotbarAndDecorations",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderJumpMeter(Lnet/minecraft/world/entity/PlayerRideableJumping;Lnet/minecraft/client/gui/GuiGraphics;I)V"))
    private void platform$jumpMeterLayer(Gui instance, PlayerRideableJumping rideable, GuiGraphics guiGraphics, int x, Operation<Void> original, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.INFO_BAR).render(guiGraphics, deltaTracker, (ctx, tc) -> original.call(instance, rideable, ctx, x));
    }
    
    @WrapOperation(method = "renderHotbarAndDecorations",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;isExperienceBarVisible()Z"))
    private boolean platform$experienceBarLayer(Gui instance, Operation<Boolean> original, @Local(argsOnly = true) GuiGraphics guiGraphics, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.INFO_BAR).render(guiGraphics, deltaTracker, (ctx, tc) -> {
            if (original.call(instance)) {
                int x = ctx.guiWidth() / 2 - 91;
                this.renderExperienceBar(ctx, x);
            }
        });
        return false;
    }
    
    @WrapOperation(method = "renderHotbarAndDecorations",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderVehicleHealth(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void platform$mountHealthLayer(Gui instance, GuiGraphics guiGraphics, Operation<Void> original, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.MOUNT_HEALTH).render(guiGraphics, deltaTracker, (ctx, tc) -> original.call(instance, ctx));
    }
    
    @WrapOperation(method = "renderHotbarAndDecorations",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void platform$itemTooltipLayer(Gui instance, GuiGraphics guiGraphics, Operation<Void> original, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.HELD_ITEM_TOOLTIP).render(guiGraphics, deltaTracker, (ctx, tc) -> original.call(instance, ctx));
    }
    
    @WrapOperation(method = "renderHotbarAndDecorations",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/spectator/SpectatorGui;renderTooltip(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void platform$spectatorTooltipLayer(SpectatorGui instance, GuiGraphics guiGraphics, Operation<Void> original, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.SPECTATOR_TOOLTIP).render(guiGraphics, deltaTracker, (ctx, tc) -> original.call(instance, ctx));
    }
    
    @WrapOperation(method = "renderPlayerHealth",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderArmor(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIII)V"))
    private void platform$armorBarLayer(GuiGraphics guiGraphics, Player player, int y, int heartRows, int height, int x, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.ARMOR_BAR).render(guiGraphics, this.minecraft.getTimer(), (ctx, tc) -> original.call(ctx, player, y, heartRows, height, x));
    }
    
    @WrapOperation(method = "renderPlayerHealth",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"))
    private void platform$healthBarLayer(Gui instance, GuiGraphics guiGraphics, Player player, int x, int y, int height, int offsetHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorption, boolean blinking, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.HEALTH_BAR).render(guiGraphics, this.minecraft.getTimer(), (ctx, tc) -> original.call(instance, ctx, player, x, y, height, offsetHeartIndex, maxHealth, currentHealth, displayHealth, absorption, blinking));
    }
    
    @WrapOperation(method = "renderPlayerHealth",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderFood(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;II)V"))
    private void platform$foodBarLayer(Gui instance, GuiGraphics guiGraphics, Player player, int y, int x, Operation<Void> original) {
        HudElementRegistryImpl.getRoot(VanillaHudElements.FOOD_BAR).render(guiGraphics, this.minecraft.getTimer(), (ctx, tc) -> original.call(instance, ctx, player, y, x));
    }
}
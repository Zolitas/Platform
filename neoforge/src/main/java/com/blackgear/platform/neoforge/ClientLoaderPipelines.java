package com.blackgear.platform.neoforge;

import com.blackgear.platform.Platform;
import com.blackgear.platform.client.event.FogRendering;
import com.blackgear.platform.client.event.HudRenderEvent;
import com.blackgear.platform.client.event.LocalPlayerEvents;
import com.blackgear.platform.client.event.screen.HudInteractions;
import com.blackgear.platform.client.event.screen.HudRendering;
import com.blackgear.platform.client.event.screen.TooltipEvents;
import com.blackgear.platform.client.event.screen.api.ScreenAccessImpl;
import com.blackgear.platform.common.events.TickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import static com.blackgear.platform.client.event.HudRenderEvent.*;

@EventBusSubscriber(modid = Platform.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ClientLoaderPipelines {
    // CLIENT TICKING
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onClientPreTick(ClientTickEvent.Pre event) {
        TickEvents.CLIENT_TICK_PRE.invoker().handle();
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onClientPostTick(ClientTickEvent.Post event) {
        TickEvents.CLIENT_TICK_POST.invoker().handle();
    }
    
    // HUD INPUT HANDLERS
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onHudMousePreScroll(ScreenEvent.MouseScrolled.Pre event) {
        if (HudInteractions.SCROLLING_PRE.invoker().onScrolling(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDeltaX(), event.getScrollDeltaY()).isCancelled()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onHudMousePostScroll(ScreenEvent.MouseScrolled.Post event) {
        HudInteractions.SCROLLING_POST.invoker().onScrolling(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDeltaX(), event.getScrollDeltaY());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onHudMousePreClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if (HudInteractions.CLICKING_PRE.invoker().onClicking(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton()).isCancelled()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onHudMousePostClick(ScreenEvent.MouseButtonPressed.Post event) {
        HudInteractions.CLICKING_POST.invoker().onClicking(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onHudMousePreRelease(ScreenEvent.MouseButtonReleased.Pre event) {
        if (HudInteractions.RELEASING_PRE.invoker().onReleasing(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton()).isCancelled()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onHudMousePostRelease(ScreenEvent.MouseButtonReleased.Post event) {
        HudInteractions.RELEASING_POST.invoker().onReleasing(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton());
    }
    
    // LOCAL PLAYER STATE
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        LocalPlayerEvents.ON_LOGIN.invoker().onLogin(event.getPlayer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerLeave(ClientPlayerNetworkEvent.LoggingOut event) {
        LocalPlayerEvents.ON_LOGOUT.invoker().onLogout(event.getPlayer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerRespawn(ClientPlayerNetworkEvent.Clone event) {
        LocalPlayerEvents.ON_RESPAWN.invoker().onRespawn(event.getOldPlayer(), event.getNewPlayer());
    }
    
    // TOOLTIP RENDERING
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onItemTooltipSetup(ItemTooltipEvent event) {
        TooltipEvents.ITEM_SETUP.invoker().registerTooltip(event.getItemStack(), event.getContext(), event.getFlags(), event.getToolTip());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onItemTooltipRendering(RenderTooltipEvent.Pre event) {
        if (TooltipEvents.RENDER_TOOLTIP.invoker().onRendering(event.getGraphics(), event.getComponents(), event.getX(), event.getY()).isCancelled()) {
            event.setCanceled(true);
        }
    }
    
    // SCREEN RENDERING
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onGuiRendering(RenderGuiEvent.Post event) {
        HudRendering.RENDERING.invoker().onRender(Minecraft.getInstance(), event.getGuiGraphics(), event.getPartialTick());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onScreenPreInitialization(ScreenEvent.Init.Pre event) {
        if (HudRendering.PRE_INITIALIZE.invoker().onInitialize(Minecraft.getInstance(), event.getScreen(), new ScreenAccessImpl(event.getScreen())).isCancelled()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onScreenPostInitialization(ScreenEvent.Init.Post event) {
        HudRendering.POST_INITIALIZE.invoker().onInitialize(Minecraft.getInstance(), event.getScreen(), new ScreenAccessImpl(event.getScreen()));
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onScreenPreRendering(ScreenEvent.Render.Pre event) {
        if (HudRendering.PRE_RENDERING.invoker().onRender(Minecraft.getInstance(), event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), Minecraft.getInstance().getTimer()).isCancelled()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onScreenPostRendering(ScreenEvent.Render.Post event) {
        HudRendering.POST_RENDERING.invoker().onRender(Minecraft.getInstance(), event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), Minecraft.getInstance().getTimer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBackgroundRendering(ContainerScreenEvent.Render.Background event) {
        HudRendering.RENDER_BACKGROUND.invoker().onRender(Minecraft.getInstance(), event.getContainerScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), Minecraft.getInstance().getTimer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onForegroundRendering(ContainerScreenEvent.Render.Foreground event) {
        HudRendering.RENDER_FOREGROUND.invoker().onRender(Minecraft.getInstance(), event.getContainerScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), Minecraft.getInstance().getTimer());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onContainerOpening(ScreenEvent.Opening event) {
        HudRendering.OPEN_CONTAINER.invoker().onOpen(Minecraft.getInstance(), event.getNewScreen());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onContainerClosing(ScreenEvent.Closing event) {
        HudRendering.CLOSE_CONTAINER.invoker().onClose(Minecraft.getInstance(), event.getScreen());
    }
    
    // HUD RENDERING
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onUserInterfaceOverlayRenderer(RenderGuiLayerEvent.Post event) {
        GuiGraphics matrices = event.getGuiGraphics();
        float tickDelta = event.getPartialTick().getGameTimeDeltaTicks();
        Minecraft minecraft = Minecraft.getInstance();
        RenderContext context = RenderContext.DEFAULT;
        
        RenderHud renderer = HudRenderEvent.RENDER_HUD.invoker();
        if (Minecraft.useFancyGraphics()) renderer.render(matrices, tickDelta, ElementType.VIGNETTE, context);
        if (minecraft.gameMode.canHurtPlayer()) renderer.render(matrices, tickDelta, ElementType.HEALTH, context);
        if (minecraft.gameMode.hasExperience()) renderer.render(matrices, tickDelta, ElementType.EXPERIENCE, context);
        if (minecraft.options.getCameraType().isFirstPerson()) renderer.render(matrices, tickDelta, ElementType.FIRST_PERSON, context);
        renderer.render(matrices, tickDelta, ElementType.DEFAULT, context);
    }
    
    // FOG RENDERING
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onFogColorCompute(ViewportEvent.ComputeFogColor event) {
        FogRendering.ColorData data = new FogRendering.ColorData(event.getCamera(), event.getRed(), event.getGreen(), event.getBlue());
        FogRendering.FOG_COLOR.invoker().setColor(data, (float) event.getPartialTick());
        event.setRed(data.getRed());
        event.setGreen(data.getGreen());
        event.setBlue(data.getBlue());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onFogDensityCompute(ViewportEvent.RenderFog event) {
        float density = FogRendering.FOG_DENSITY.invoker().setDensity(event.getCamera(), 0.1F);
        if (density != 0.1F) {
            event.setNearPlaneDistance(-8.0F);
            event.setFarPlaneDistance(density * 0.5F);
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onFogRenderer(ViewportEvent.RenderFog event) {
        FogRendering.FogData data = new FogRendering.FogData(event.getNearPlaneDistance(), event.getFarPlaneDistance(), event.getFogShape());
        if (FogRendering.FOG_RENDER.invoker().onFogRender(event.getMode(), event.getType(), event.getCamera(), (float) event.getPartialTick(), event.getRenderer().getRenderDistance(), event.getFarPlaneDistance(), event.getNearPlaneDistance(), event.getFogShape(), data).isCancelled()) {
            event.setNearPlaneDistance(data.getNearPlaneDistance());
            event.setFarPlaneDistance(data.getFarPlaneDistance());
            event.setFogShape(data.getShape());
        }
    }
}
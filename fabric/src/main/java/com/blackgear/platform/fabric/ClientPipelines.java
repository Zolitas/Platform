package com.blackgear.platform.fabric;

import com.blackgear.platform.client.event.HudRenderEvent;
import com.blackgear.platform.client.event.LocalPlayerEvents;
import com.blackgear.platform.client.event.rendering.LivingEntityRendererCallback;
import com.blackgear.platform.client.event.screen.HudRendering;
import com.blackgear.platform.client.event.screen.TooltipEvents;
import com.blackgear.platform.common.events.TickEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class ClientPipelines {
    public static void bootstrap() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.player != null) LocalPlayerEvents.ON_LOGIN.invoker().onLogin(client.player);
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (client.player != null) LocalPlayerEvents.ON_LOGOUT.invoker().onLogout(client.player);
        });
        
        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> TickEvents.CLIENT_TICK_PRE.invoker().handle());
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> TickEvents.CLIENT_TICK_POST.invoker().handle());

        ClientTickEvents.START_WORLD_TICK.register(TickEvents.LEVEL_TICK_PRE.invoker()::handle);
        ClientTickEvents.END_WORLD_TICK.register(TickEvents.LEVEL_TICK_POST.invoker()::handle);
        
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entity, renderer, appender, context) -> LivingEntityRendererCallback.APPEND_LAYERS.invoker().register(entity, renderer, appender::register, context));
        
        renderHudEvent();
        renderTooltipEvent();
    }

    private static void renderTooltipEvent() {
        ItemTooltipCallback.EVENT.register((stack, flags, components) -> TooltipEvents.ITEM_SETUP.invoker().registerTooltip(stack, components, flags));
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> HudRendering.RENDERING.invoker().onRender(Minecraft.getInstance(), graphics, tickDelta));
    }

    private static void renderHudEvent() {
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            Minecraft minecraft = Minecraft.getInstance();
            HudRenderEvent.RenderContext context = HudRenderEvent.RenderContext.DEFAULT;

            if (Minecraft.useFancyGraphics()) {
                HudRenderEvent.RENDER_HUD.invoker().render(matrices, tickDelta, HudRenderEvent.ElementType.VIGNETTE, context);
            }

            if (minecraft.gameMode.canHurtPlayer()) {
                HudRenderEvent.RENDER_HUD.invoker().render(matrices, tickDelta, HudRenderEvent.ElementType.HEALTH, context);
            }

            if (minecraft.gameMode.hasExperience()) {
                HudRenderEvent.RENDER_HUD.invoker().render(matrices, tickDelta, HudRenderEvent.ElementType.EXPERIENCE, context);
            }

            if (minecraft.options.getCameraType().isFirstPerson()) {
                HudRenderEvent.RENDER_HUD.invoker().render(matrices, tickDelta, HudRenderEvent.ElementType.FIRST_PERSON, context);
            }

            HudRenderEvent.RENDER_HUD.invoker().render(matrices, tickDelta, HudRenderEvent.ElementType.DEFAULT, context);
        });
    }
}
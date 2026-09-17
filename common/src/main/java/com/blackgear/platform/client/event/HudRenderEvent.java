package com.blackgear.platform.client.event;

import com.blackgear.platform.core.util.event.Event;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class HudRenderEvent {
    public static final Event<RenderHud> RENDER_HUD = Event.create(RenderHud.class);
    
    @FunctionalInterface
    public interface RenderHud {
        void render(GuiGraphics matrices, float tickDelta, ElementType type, RenderContext context);
    }

    public interface RenderContext {
        RenderContext DEFAULT = new RenderContext() {};
        
        default Window window() {
            return this.minecraft().getWindow();
        }

        default int screenWidth() {
            return this.window().getGuiScaledWidth();
        }

        default int screenHeight() {
            return this.window().getGuiScaledHeight();
        }

        default Minecraft minecraft() {
            return Minecraft.getInstance();
        }

        default LocalPlayer player() {
            return this.minecraft().player;
        }

        default Gui gui() {
            return this.minecraft().gui;
        }
    }

    public enum ElementType {
        DEFAULT, HEALTH, EXPERIENCE, FIRST_PERSON, VIGNETTE
    }
}
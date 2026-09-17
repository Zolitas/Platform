package com.blackgear.platform.client.event.screen.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

public interface HudElement {
    void render(GuiGraphics graphics, DeltaTracker timer);
}
package com.blackgear.platform.client.event.screen.hud;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public interface HudLayer {
	ResourceLocation id();

	HudElement element(HudElement element);

	boolean isRemoved();

	static HudLayer ofVanilla(ResourceLocation id) {
		return of(id, Function.identity(), false);
	}

	static HudLayer ofElement(ResourceLocation id, HudElement element) {
		return of(id, operator -> element, false);
	}

	static HudLayer of(ResourceLocation id, Function<HudElement, HudElement> operator, boolean removed) {
		return new HudLayer() {
			@Override
			public ResourceLocation id() {
				return id;
			}

			@Override
			public HudElement element(HudElement element) {
				return operator.apply(element);
			}

			@Override
			public boolean isRemoved() {
				return removed;
			}
		};
	}
}

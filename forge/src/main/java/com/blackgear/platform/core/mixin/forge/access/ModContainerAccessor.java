package com.blackgear.platform.core.mixin.forge.access;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.EnumMap;

@Mixin(ModContainer.class)
public interface ModContainerAccessor {
    @Accessor EnumMap<ModConfig.Type, ModConfig> getConfigs();
}
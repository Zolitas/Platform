package com.blackgear.platform.common.entity.forge;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;

import java.util.function.Supplier;

public class ReachAttributesImpl {
    public static Supplier<Attribute> getBlockInteractionReachAttribute() {
        return ForgeMod.BLOCK_REACH;
    }
    
    public static Supplier<Attribute> getEntityInteractionReachAttribute() {
        return ForgeMod.ENTITY_REACH;
    }
}

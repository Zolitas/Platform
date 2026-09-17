package com.blackgear.platform.common.entity;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.function.Supplier;

public class ReachAttributes {
    @ExpectPlatform
    public static Supplier<Attribute> getBlockInteractionReachAttribute() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static Supplier<Attribute> getEntityInteractionReachAttribute() {
        throw new AssertionError();
    }
}
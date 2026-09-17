package com.blackgear.platform.common.entity.fabric;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.function.Supplier;

public class ReachAttributesImpl {
    public static Supplier<Attribute> getBlockInteractionReachAttribute() {
        return () -> ReachEntityAttributes.REACH;
    }
    
    public static Supplier<Attribute> getEntityInteractionReachAttribute() {
        return () -> ReachEntityAttributes.ATTACK_RANGE;
    }
}
package com.blackgear.platform.common.v2.entity.impl;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.Set;

public interface AttributeUpdater {
    Set<AttributeInstance> platform$getAttributesToUpdate();
}
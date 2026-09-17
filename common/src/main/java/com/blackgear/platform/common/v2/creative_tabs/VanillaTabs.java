package com.blackgear.platform.common.v2.creative_tabs;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

/**
 * <p>This enum provides a stable API for referencing
 * vanilla creative tabs without requiring direct access
 * to {@link CreativeModeTabs}.</p>
 *
 * <p>Each constant is associated with the corresponding
 * vanilla creative tab resource key.</p>
 *
 * @author ItsBlackGear
 */
public enum VanillaTabs {
    BUILDING_BLOCKS(CreativeModeTabs.BUILDING_BLOCKS),
    COLORED_BLOCKS(CreativeModeTabs.COLORED_BLOCKS),
    NATURAL_BLOCKS(CreativeModeTabs.NATURAL_BLOCKS),
    FUNCTIONAL_BLOCKS(CreativeModeTabs.FUNCTIONAL_BLOCKS),
    REDSTONE_BLOCKS(CreativeModeTabs.REDSTONE_BLOCKS),
    TOOLS_AND_UTILITIES(CreativeModeTabs.TOOLS_AND_UTILITIES),
    COMBAT(CreativeModeTabs.COMBAT),
    FOOD_AND_DRINKS(CreativeModeTabs.FOOD_AND_DRINKS),
    INGREDIENTS(CreativeModeTabs.INGREDIENTS),
    SPAWN_EGGS(CreativeModeTabs.SPAWN_EGGS),
    OP_BLOCKS(CreativeModeTabs.OP_BLOCKS);
    
    private final ResourceKey<CreativeModeTab> key;
    
    VanillaTabs(ResourceKey<CreativeModeTab> key) {
        this.key = key;
    }
    
    public ResourceKey<CreativeModeTab> key() {
        return this.key;
    }
}
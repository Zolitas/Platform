package com.blackgear.platform.common.v2.creative_tabs;

import net.minecraft.world.flag.FeatureFlagSet;

/**
 * Modifies the contents of a creative tab.
 */
@FunctionalInterface
public interface Modifier {
    /**
     * Applies modifications to a creative tab.
     *
     * @param flags enabled feature flags
     * @param output output used to modify the tab
     * @param operator whether operator-only entries are visible
     */
    void accept(FeatureFlagSet flags, Output output, boolean operator);
}
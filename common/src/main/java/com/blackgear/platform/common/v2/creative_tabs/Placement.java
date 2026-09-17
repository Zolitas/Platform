package com.blackgear.platform.common.v2.creative_tabs;

import net.minecraft.world.level.ItemLike;

import java.util.Collection;

public final class Placement {
    private final Output output;
    private final boolean after;
    private ItemLike current;
    
    Placement(
        Output output,
        ItemLike target,
        boolean after
    ) {
        this.output = output;
        this.current = target;
        this.after = after;
    }
    
    public Placement add(ItemLike item) {
        if (after) {
            output.addAfter(current, item);
        } else {
            output.addBefore(current, item);
        }
        
        current = item;
        
        return this;
    }
    
    public Placement add(ItemLike... items) {
        for (ItemLike item : items) {
            add(item);
        }
        
        return this;
    }
    
    public Placement add(
        Collection<? extends ItemLike> items
    ) {
        for (ItemLike item : items) {
            add(item);
        }
        
        return this;
    }
}
package com.blackgear.platform.common.v2.creative_tabs;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Output used to modify the contents of a creative tab.
 *
 * <p>Supports direct insertion, relative insertion, and fluent placement
 * builders for bulk item additions.</p>
 */
public interface Output extends CreativeModeTab.Output {
    /**
     * Returns whether the specified item is present
     * in the current creative tab.
     */
    boolean contains(ItemLike item);
    
    /**
     * Returns whether the specified stack is present
     * in the current creative tab.
     */
    default boolean contains(ItemStack stack) {
        return contains(stack.getItem());
    }
    
    default ItemStack sanitize(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (stack.getCount() != 1) return stack.copyWithCount(1);
        return stack;
    }
    
    // ============================================================
    // DIRECT ADDITIONS
    // ============================================================
    
    /**
     * Adds an item to the end of the tab.
     */
    default void add(ItemLike item) {
        accept(item.asItem().getDefaultInstance());
    }
    
    /**
     * Adds a stack to the end of the tab.
     */
    default void add(ItemStack stack) {
        accept(stack);
    }
    
    /**
     * Adds a stack using the specified visibility.
     */
    default void add(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
        accept(stack, visibility);
    }
    
    /**
     * Adds an item if it is not already present.
     */
    default boolean addIfAbsent(ItemLike item) {
        if (!contains(item)) {
            add(item);
            return true;
        }
        
        return false;
    }
    
    // ============================================================
    // LOW LEVEL INSERTION
    // ============================================================
    
    /**
     * Inserts an item immediately after another item.
     */
    void addAfter(
        ItemStack target,
        ItemStack stack,
        CreativeModeTab.TabVisibility visibility
    );
    
    /**
     * Inserts an item immediately before another item.
     */
    void addBefore(
        ItemStack target,
        ItemStack stack,
        CreativeModeTab.TabVisibility visibility
    );
    
    default void addAfter(ItemLike target, ItemLike stack) {
        addAfter(
            target.asItem().getDefaultInstance(),
            stack.asItem().getDefaultInstance(),
            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
        );
    }
    
    default void addBefore(ItemLike target, ItemLike stack) {
        addBefore(
            target.asItem().getDefaultInstance(),
            stack.asItem().getDefaultInstance(),
            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
        );
    }
    
    /**
     * Inserts an item after the target if the item is not already present.
     */
    default boolean addAfterIfAbsent(ItemLike target, ItemLike stack) {
        if (!contains(stack)) {
            addAfter(target, stack);
            return true;
        }
        
        return false;
    }
    
    /**
     * Inserts an item before the target if the item is not already present.
     */
    default boolean addBeforeIfAbsent(ItemLike target, ItemLike stack) {
        if (!contains(stack)) {
            addBefore(target, stack);
            return true;
        }
        
        return false;
    }
    
    // ============================================================
    // BULK HELPERS
    // ============================================================
    
    default void addAllAfter(
        ItemLike target,
        Collection<? extends ItemLike> items
    ) {
        List<ItemLike> reversed = new ArrayList<>(items);
        Collections.reverse(reversed);
        
        for (ItemLike item : reversed) {
            addAfter(target, item);
        }
    }
    
    default void addAllAfter(
        ItemLike target,
        ItemLike... items
    ) {
        for (int i = items.length - 1; i >= 0; i--) {
            addAfter(target, items[i]);
        }
    }
    
    default void addAllBefore(
        ItemLike target,
        Collection<? extends ItemLike> items
    ) {
        for (ItemLike item : items) {
            addBefore(target, item);
        }
    }
    
    default void addAllBefore(
        ItemLike target,
        ItemLike... items
    ) {
        for (ItemLike item : items) {
            addBefore(target, item);
        }
    }
    
    // ============================================================
    // FLUENT API
    // ============================================================
    
    /**
     * Creates a placement builder that inserts items
     * immediately after the specified target.
     */
    default Placement after(ItemLike target) {
        return new AfterPlacement(this, target);
    }
    
    /**
     * Creates a placement builder that inserts items
     * immediately before the specified target.
     */
    default Placement before(ItemLike target) {
        return new BeforePlacement(this, target);
    }
    
    /**
     * Fluent placement builder.
     */
    interface Placement {
        Placement add(ItemLike item);
        
        default Placement add(ItemLike... items) {
            for (ItemLike item : items) {
                add(item);
            }
            
            return this;
        }
        
        default Placement add(Collection<? extends ItemLike> items) {
            for (ItemLike item : items) {
                add(item);
            }
            
            return this;
        }
        
        default <T> Placement add(Iterable<T> values, Function<T, ? extends ItemLike> mapper) {
            for (T value : values) {
                add(mapper.apply(value));
            }
            
            return this;
        }
    }

    /**
     * Placement builder that chains items after
     * the previously inserted item.
     */
    final class AfterPlacement implements Placement {
        private final Output output;
        private ItemLike current;

        AfterPlacement(Output output, ItemLike target) {
            this.output = output;
            this.current = target;
        }

        @Override
        public Placement add(ItemLike item) {
            if (!output.contains(item)) output.addAfter(current, item);
            current = item;
            return this;
        }
    }

    /**
     * Placement builder that always inserts items
     * before the original target.
     */
    final class BeforePlacement implements Placement {
        private final Output output;
        private final ItemLike anchor;

        BeforePlacement(Output output, ItemLike target) {
            this.output = output;
            this.anchor = target;
        }

        @Override
        public Placement add(ItemLike item) {
            if (!output.contains(item)) output.addBefore(anchor, item);
            return this;
        }
    }
}
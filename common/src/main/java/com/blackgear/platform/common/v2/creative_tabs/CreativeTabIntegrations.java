package com.blackgear.platform.common.v2.creative_tabs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Registry for creative tab integrations.
 *
 * <p>This API allows mods to contribute items to both vanilla and
 * custom creative tabs in a platform-independent way.</p>
 *
 * <p>Integrations are registered during mod initialization and are
 * automatically applied on all supported platforms.</p>
 *
 * <pre>{@code
 * CreativeTabIntegrations.register(event -> {
 *     event.register(VanillaTabs.INGREDIENTS, (flags, output, operator) -> {
 *         output.addAfter(
 *             Items.EGG,
 *             ModItems.MY_CUSTOM_EGG
 *         );
 *     });
 * });
 * }</pre>
 *
 * @author ItsBlackGear
 */
public final class CreativeTabIntegrations {
    private static final Map<ResourceKey<CreativeModeTab>, List<Modifier>> MODIFIERS = new LinkedHashMap<>();
    private static Consumer<ResourceKey<CreativeModeTab>> listener;
    
    public static void register(Consumer<Event> consumer) {
        consumer.accept(new Event() {
            @Override
            public void register(ResourceKey<CreativeModeTab> tab, Modifier modifier) {
                MODIFIERS.computeIfAbsent(tab, key -> new ArrayList<>()).add(modifier);
                if (listener != null) listener.accept(tab);
            }
            
            @Override
            public void register(VanillaTabs tab, Modifier modifier) {
                this.register(tab.key(), modifier);
            }
            
            @Override
            public void register(CreativeModeTab tab, Modifier modifier) {
                ResourceKey<CreativeModeTab> key = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).orElseThrow();
                this.register(key, modifier);
            }
        });
    }
    
    public static void setListener(Consumer<ResourceKey<CreativeModeTab>> listener) {
        CreativeTabIntegrations.listener = listener;
    }
    
    public static Map<ResourceKey<CreativeModeTab>, List<Modifier>> modifiers() {
        return MODIFIERS;
    }
    
    /**
     * Registration context for creative tab integrations.
     */
    public interface Event {
        /**
         * Registers a modifier for a vanilla creative tab.
         */
        void register(VanillaTabs tab, Modifier modifier);
        
        /**
         * Registers a modifier for a creative tab instance.
         */
        void register(CreativeModeTab tab, Modifier modifier);
        
        /**
         * Registers a modifier for a creative tab key.
         */
        void register(ResourceKey<CreativeModeTab> tab, Modifier modifier);
    }
}
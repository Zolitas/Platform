package com.blackgear.platform.client.event.screen.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HudElementRegistryImpl {
    static final List<ResourceLocation> VANILLA_ELEMENT_IDS = List.of(
        VanillaHudElements.MISC_OVERLAYS,
        VanillaHudElements.CROSSHAIR,
        VanillaHudElements.SPECTATOR_MENU,
        VanillaHudElements.HOTBAR,
        VanillaHudElements.ARMOR_BAR,
        VanillaHudElements.HEALTH_BAR,
        VanillaHudElements.FOOD_BAR,
        VanillaHudElements.AIR_BAR,
        VanillaHudElements.MOUNT_HEALTH,
        VanillaHudElements.INFO_BAR,
        VanillaHudElements.EXPERIENCE_LEVEL,
        VanillaHudElements.HELD_ITEM_TOOLTIP,
        VanillaHudElements.SPECTATOR_TOOLTIP,
        VanillaHudElements.STATUS_EFFECTS,
        VanillaHudElements.BOSS_BAR,
        VanillaHudElements.SLEEP,
        VanillaHudElements.DEMO_TIMER,
        VanillaHudElements.DEBUG,
        VanillaHudElements.SCOREBOARD,
        VanillaHudElements.OVERLAY_MESSAGE,
        VanillaHudElements.TITLE_AND_SUBTITLE,
        VanillaHudElements.CHAT,
        VanillaHudElements.PLAYER_LIST,
        VanillaHudElements.SUBTITLES
    );
    public static final Map<ResourceLocation, RootLayer> ROOT_ELEMENTS = VANILLA_ELEMENT_IDS.stream()
        .map(RootLayer::new)
        .collect(Collectors.toMap(RootLayer::id, Function.identity(), (a, b) -> a, IdentityHashMap::new));
    private static final RootLayer FIRST = ROOT_ELEMENTS.get(VanillaHudElements.MISC_OVERLAYS);
    private static final RootLayer LAST = ROOT_ELEMENTS.get(VanillaHudElements.SUBTITLES);
    
    public static RootLayer getRoot(ResourceLocation id) {
        return ROOT_ELEMENTS.get(id);
    }
    
    public static void addFirst(ResourceLocation id, HudElement element) {
        validateUnique(id);
        FIRST.layers().addFirst(HudLayer.ofElement(id, element));
    }
    
    public static void addLast(ResourceLocation id, HudElement element) {
        validateUnique(id);
        LAST.layers().addLast(HudLayer.ofElement(id, element));
    }
    
    public static void attachElementBefore(ResourceLocation beforeThis, ResourceLocation id, HudElement element) {
        validateUnique(id);
        
        boolean didChange = findLayer(beforeThis, (l, iterator) -> {
            iterator.previous();
            iterator.add(HudLayer.ofElement(id, element));
            iterator.next();
            return true;
        });
        
        if (!didChange) {
            throw new IllegalArgumentException("Layer with identifier " + beforeThis + " not found");
        }
    }
    
    public static void attachElementAfter(ResourceLocation afterThis, ResourceLocation id, HudElement element) {
        validateUnique(id);
        
        boolean didChange = findLayer(afterThis, (l, iterator) -> {
            iterator.add(HudLayer.ofElement(id, element));
            return true;
        });
        
        if (!didChange) {
            throw new IllegalArgumentException("Layer with identifier " + afterThis + " not found");
        }
    }
    
    public static void removeElement(ResourceLocation identifier) {
        boolean didChange = findLayer(identifier, (l, iterator) -> {
            iterator.set(HudLayer.of(l.id(), l::element, true));
            return true;
        });
        
        if (!didChange) {
            throw new IllegalArgumentException("Layer with identifier " + identifier + " not found");
        }
    }
    
    public static void replaceElement(ResourceLocation identifier, Function<HudElement, HudElement> replacer) {
        boolean didChange = findLayer(identifier, (l, iterator) -> {
            iterator.set(HudLayer.of(l.id(), replacer.compose(l::element), l.isRemoved()));
            return true;
        });
        
        if (!didChange) {
            throw new IllegalArgumentException("Layer with identifier " + identifier + " not found");
        }
    }
    
    static void validateUnique(ResourceLocation id) {
        visitLayers((l, iterator) -> {
            if (l.id().equals(id)) {
                throw new IllegalArgumentException("Layer with identifier " + id + " already exists");
            }
            
            return false;
        });
    }
    
    static boolean findLayer(ResourceLocation identifier, LayerVisitor visitor) {
        MutableBoolean found = new MutableBoolean(false);
        
        visitLayers((l, iterator) -> {
            if (l.id().equals(identifier)) {
                found.setTrue();
                return visitor.visit(l, iterator);
            }
            
            return false;
        });
        
        return found.booleanValue();
    }
    
    static boolean visitLayers(LayerVisitor visitor) {
        boolean modified = false;
        
        for (ResourceLocation id : VANILLA_ELEMENT_IDS) {
            RootLayer rootLayer = ROOT_ELEMENTS.get(id);
            modified |= visitLayers(rootLayer.layers(), visitor);
        }
        
        return modified;
    }
    
    private static boolean visitLayers(List<HudLayer> layers, LayerVisitor visitor) {
        MutableBoolean modified = new MutableBoolean(false);
        ListIterator<HudLayer> iterator = layers.listIterator();
        
        while (iterator.hasNext()) {
            HudLayer layer = iterator.next();
            
            if (visitor.visit(layer, iterator)) {
                modified.setTrue();
            }
        }
        
        return modified.booleanValue();
    }
    
    interface LayerVisitor {
        boolean visit(HudLayer layer, ListIterator<HudLayer> iterator);
    }
    
    public record RootLayer(ResourceLocation id, List<HudLayer> layers) {
        private RootLayer(ResourceLocation id) {
            this(id, new ArrayList<>());
            layers().add(HudLayer.ofVanilla(id));
        }
        
        public void render(GuiGraphics context, DeltaTracker tickCounter, HudElement vanillaElement) {
            for (HudLayer layer : layers) {
                if (!layer.isRemoved()) {
                    layer.element(vanillaElement).render(context, tickCounter);
                }
            }
        }
    }
}
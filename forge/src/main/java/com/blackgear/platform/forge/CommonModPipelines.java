package com.blackgear.platform.forge;

import com.blackgear.platform.Platform;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Platform.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CommonModPipelines {
    // MOB PIPELINES
    public static final EventPipeline<EntityAttributeCreationEvent> MOB_ATTRIBUTE = new EventPipeline<>();
    public static final EventPipeline<SpawnPlacementRegisterEvent> SPAWN_PLACEMENT = new EventPipeline<>();
    
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        MOB_ATTRIBUTE.dispatch(event);
    }
    
    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        SPAWN_PLACEMENT.dispatch(event);
    }
}

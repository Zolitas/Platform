package com.blackgear.platform.neoforge;

import com.blackgear.platform.Platform;
import com.blackgear.platform.client.event.rendering.LivingEntityRendererCallback;
import com.blackgear.platform.client.v2.impl.LayerAppenderImpl;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(modid = Platform.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModPipelines {
    // COLOR PIPELINES
    public static final EventPipeline<RegisterColorHandlersEvent.Block> BLOCK_COLORS = new EventPipeline<>();
    public static final EventPipeline<RegisterColorHandlersEvent.Item> ITEM_COLORS = new EventPipeline<>();
    
    @SubscribeEvent
    public static void onBlockColorRegister(RegisterColorHandlersEvent.Block event) {
        BLOCK_COLORS.dispatch(event);
    }
    
    @SubscribeEvent
    public static void onItemColorRegister(RegisterColorHandlersEvent.Item event) {
        ITEM_COLORS.dispatch(event);
    }
    
    // ENTITY RENDERING PIPELINES
    public static final EventPipeline<EntityRenderersEvent.RegisterRenderers> RENDERERS = new EventPipeline<>();
    public static final EventPipeline<EntityRenderersEvent.RegisterLayerDefinitions> LAYER_DEFINITIONS = new EventPipeline<>();
    public static final EventPipeline<EntityRenderersEvent.CreateSkullModels> SKULL_MODELS = new EventPipeline<>();
    
    @SubscribeEvent
    public static void onRendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        RENDERERS.dispatch(event);
    }
    
    @SubscribeEvent
    public static void onLayerDefinitionRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
        LAYER_DEFINITIONS.dispatch(event);
    }
    
    @SubscribeEvent
    public static void onSkullRendererRegister(EntityRenderersEvent.CreateSkullModels event) {
        SKULL_MODELS.dispatch(event);
    }
    
    // ITEM-LIKE RENDERING PIPELINES
    public static final EventPipeline<ModelEvent.ModifyBakingResult> MODIFY_BAKING = new EventPipeline<>();
    public static final EventPipeline<ModelEvent.RegisterAdditional> REGISTER_ADDITIONAL = new EventPipeline<>();
    
    @SubscribeEvent
    public static void onModelBakingModifying(ModelEvent.ModifyBakingResult event) {
        MODIFY_BAKING.dispatch(event);
    }
    
    @SubscribeEvent
    public static void onAdditionalModelRegister(ModelEvent.RegisterAdditional event) {
        REGISTER_ADDITIONAL.dispatch(event);
    }
    
    // PARTICLE RENDERING PIPELINE
    public static final EventPipeline<RegisterParticleProvidersEvent> PARTICLE_RENDERING = new EventPipeline<>();
    
    @SubscribeEvent
    public static void onParticleRenderingRegister(RegisterParticleProvidersEvent event) {
        PARTICLE_RENDERING.dispatch(event);
    }
    
    // RESOURCE LISTENING PIPELINE
    public static final EventPipeline<RegisterClientReloadListenersEvent> RESOURCE_LISTENERS = new EventPipeline<>();
    
    @SubscribeEvent
    public static void onResourceListening(RegisterClientReloadListenersEvent event) {
        RESOURCE_LISTENERS.dispatch(event);
    }
    
    @SubscribeEvent @SuppressWarnings({"unchecked", "rawtypes"})
    public static void onLayerAppending(EntityRenderersEvent.AddLayers event) {
        for (EntityType<?> type : event.getEntityTypes()) {
            if (event.getRenderer(type) instanceof LivingEntityRenderer renderer) {
                LivingEntityRendererCallback.APPEND_LAYERS.invoker().register((EntityType<? extends LivingEntity>) type, renderer, new LayerAppenderImpl(renderer::addLayer), event.getContext());
            }
        }
        
        for (var skinName : event.getSkins()) {
            if (event.getSkin(skinName) instanceof LivingEntityRenderer renderer) {
                LivingEntityRendererCallback.APPEND_LAYERS.invoker().register(EntityType.PLAYER, renderer, new LayerAppenderImpl(renderer::addLayer), event.getContext());
            }
        }
    }
}

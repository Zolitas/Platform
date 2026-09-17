package com.blackgear.platform.client.event.rendering;

import com.blackgear.platform.core.util.event.Event;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityRendererCallback {
    Event<LivingEntityRendererCallback> APPEND_LAYERS = Event.create(LivingEntityRendererCallback.class);
    
    void register(EntityType<? extends LivingEntity> entity, LivingEntityRenderer<?, ?> renderer, LayerAppender appender, EntityRendererProvider.Context context);
    
    interface LayerAppender {
        <T extends LivingEntity> void addLayer(RenderLayer<T, ? extends EntityModel<T>> layer);
    }
}
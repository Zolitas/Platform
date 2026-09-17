package com.blackgear.platform.client.v2.impl;

import com.blackgear.platform.client.event.rendering.LivingEntityRendererCallback;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;
import java.util.function.Function;

public class LayerAppenderImpl implements LivingEntityRendererCallback.LayerAppender {
    private final Function<RenderLayer<?, ?>, Boolean> delegate;
    
    public LayerAppenderImpl(Function<RenderLayer<?, ?>, Boolean> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public <T extends LivingEntity> void addLayer(RenderLayer<T, ? extends EntityModel<T>> layer) {
        Objects.requireNonNull(layer, "Layer renderer cannot be null");
        this.delegate.apply(layer);
    }
}
package com.blackgear.platform.common.v2.entity;

import com.blackgear.platform.core.util.event.Event;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public interface EntityAttributeEvents {
    Event<LivingAttributes> ON_LIVING_ATTRIBUTE_UPDATE = Event.create(LivingAttributes.class);
    Event<PlayerAttributes> UPDATE_PLAYER_ATTRIBUTE = Event.create(PlayerAttributes.class);
    
    interface LivingAttributes {
        void onUpdate(LivingEntity entity, Holder<Attribute> attribute);
    }
    
    interface PlayerAttributes {
        void update(ServerPlayer player);
    }
}
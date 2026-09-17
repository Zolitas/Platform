package com.blackgear.platform.core.mixin.neoforge;

import com.blackgear.platform.core.mixin.neoforge.access.MergedSpawnPredicateAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = RegisterSpawnPlacementsEvent.class, remap = false)
public abstract class RegisterSpawnPlacementsEventMixin {
    @Shadow @Final private Map<EntityType<?>, RegisterSpawnPlacementsEvent.MergedSpawnPredicate<?>> map;
    
    @Inject(
        method = "register(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacementType;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;Lnet/neoforged/neoforge/event/entity/RegisterSpawnPlacementsEvent$Operation;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private <T extends Entity> void platform$patch(
        EntityType<T> entityType,
        SpawnPlacementType placementType,
        Heightmap.Types heightmap,
        SpawnPlacements.SpawnPredicate<T> predicate,
        RegisterSpawnPlacementsEvent.Operation operation,
        CallbackInfo ci
    ) {
        if (this.map.containsKey(entityType) && operation != RegisterSpawnPlacementsEvent.Operation.REPLACE) {
            ((MergedSpawnPredicateAccessor) this.map.get(entityType)).callMerge(operation, predicate, placementType, heightmap);
            ci.cancel();
        }
    }
}
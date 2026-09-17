package com.blackgear.platform.core.mixin.forge;

import com.blackgear.platform.core.mixin.forge.access.MergedSpawnPredicateAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = SpawnPlacementRegisterEvent.class, remap = false)
public abstract class SpawnPlacementRegisterEventMixin {
    @Shadow @Final private Map<EntityType<?>, SpawnPlacementRegisterEvent.MergedSpawnPredicate<?>> map;
    
    @Inject(
        method = "register(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;Lnet/minecraftforge/event/entity/SpawnPlacementRegisterEvent$Operation;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private <T extends Entity> void platform$patchSpawnPlacements(
        EntityType<T> entityType,
        @Nullable SpawnPlacements.Type placementType,
        @Nullable Heightmap.Types heightmap,
        SpawnPlacements.SpawnPredicate<T> predicate,
        SpawnPlacementRegisterEvent.Operation operation,
        CallbackInfo ci
    ) {
        if (this.map.containsKey(entityType) && operation != SpawnPlacementRegisterEvent.Operation.REPLACE) {
            ((MergedSpawnPredicateAccessor) this.map.get(entityType)).callMerge(operation, predicate, placementType, heightmap);
            ci.cancel();
        }
    }
}
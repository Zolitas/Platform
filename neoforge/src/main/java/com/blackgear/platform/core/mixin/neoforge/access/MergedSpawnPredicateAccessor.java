package com.blackgear.platform.core.mixin.neoforge.access;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.MergedSpawnPredicate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MergedSpawnPredicate.class)
public interface MergedSpawnPredicateAccessor {
    @Invoker <T extends Entity> void callMerge(RegisterSpawnPlacementsEvent.Operation operation, SpawnPlacements.SpawnPredicate<T> predicate, @Nullable SpawnPlacementType spawnType, @Nullable Heightmap.Types heightmapType);
}
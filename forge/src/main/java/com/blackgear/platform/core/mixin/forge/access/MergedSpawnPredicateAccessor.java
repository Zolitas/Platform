package com.blackgear.platform.core.mixin.forge.access;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent.MergedSpawnPredicate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MergedSpawnPredicate.class)
public interface MergedSpawnPredicateAccessor {
    @Invoker <T extends Entity> void callMerge(SpawnPlacementRegisterEvent.Operation operation, SpawnPlacements.SpawnPredicate<T> predicate, @Nullable SpawnPlacements.Type spawnType, @Nullable Heightmap.Types heightmapType);
}
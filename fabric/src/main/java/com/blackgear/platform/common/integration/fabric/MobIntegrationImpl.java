package com.blackgear.platform.common.integration.fabric;

import com.blackgear.platform.common.integration.MobIntegration;
import com.blackgear.platform.common.integration.MobInteraction;
import com.blackgear.platform.common.integration.v2.spawn_placement.SpawnPlacementStrategy;
import com.blackgear.platform.core.mixin.access.SpawnPlacementsAccessor;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MobIntegrationImpl {
    public static void registerIntegrations(Consumer<MobIntegration.Event> listener) {
        listener.accept(new MobIntegration.Event() {
            private final Map<EntityType<?>, MergedSpawnPredicate<?>> map = new HashMap<>();
            
            @Override
            public void registerMobInteraction(MobInteraction interaction) {
                UseEntityCallback.EVENT.register((player, level, hand, entity, hit) -> interaction.onInteract(player, entity, hand));
            }
            
            @Override
            public void registerAttributes(Supplier<? extends EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> builder) {
                FabricDefaultAttributeRegistry.register(type.get(), builder.get());
            }
            
            @Override
            @SuppressWarnings("unchecked")
            public <T extends Mob> void registerPlacement(
                Supplier<EntityType<T>> type,
                @Nullable SpawnPlacementType placementType,
                @Nullable Heightmap.Types heightmap,
                SpawnPlacements.SpawnPredicate<T> predicate,
                SpawnPlacementStrategy operation
            ) {
                EntityType<T> entity = type.get();
                
                if (!map.containsKey(entity)) {
                    SpawnPlacements.Data existing = SpawnPlacementsAccessor.getDATA_BY_TYPE().get(entity);
                    
                    if (existing == null) {
                        if (placementType == null) {
                            throw new NullPointerException("Registering a new Spawn Predicate requires a nonnull placement type! Entity Type: " + BuiltInRegistries.ENTITY_TYPE.getKey(entity));
                        }
                        if (heightmap == null) {
                            throw new NullPointerException("Registering a new Spawn Predicate requires a nonnull heightmap type! Entity Type: " + BuiltInRegistries.ENTITY_TYPE.getKey(entity));
                        }
                        map.put(entity, new MergedSpawnPredicate<>(predicate, placementType, heightmap));
                    } else {
                        MergedSpawnPredicate<T> merged = new MergedSpawnPredicate<>((SpawnPlacements.SpawnPredicate<T>) existing.predicate(), existing.placement(), existing.heightMap());
                        merged.merge(operation, predicate, placementType, heightmap);
                        map.put(entity, merged);
                    }
                } else {
                    ((MergedSpawnPredicate<T>) map.get(entity)).merge(operation, predicate, placementType, heightmap);
                }
                
                MergedSpawnPredicate<T> merged = (MergedSpawnPredicate<T>) map.get(entity);
                SpawnPlacementsAccessor.getDATA_BY_TYPE().put(entity, new SpawnPlacements.Data(merged.getHeightmapType(), merged.getSpawnType(), merged.build()));
            }
        });
    }
    
    private static class MergedSpawnPredicate<T extends Mob> {
        private final SpawnPlacements.SpawnPredicate<T> originalPredicate;
        private final List<SpawnPlacements.SpawnPredicate<T>> orPredicates = new ArrayList<>();
        private final List<SpawnPlacements.SpawnPredicate<T>> andPredicates = new ArrayList<>();
        private @Nullable SpawnPlacements.SpawnPredicate<T> replacementPredicate;
        private SpawnPlacementType spawnType;
        private Heightmap.Types heightmapType;
        
        public MergedSpawnPredicate(
            SpawnPlacements.SpawnPredicate<T> originalPredicate,
            SpawnPlacementType spawnType,
            Heightmap.Types heightmapType
        ) {
            this.originalPredicate = originalPredicate;
            this.spawnType = spawnType;
            this.heightmapType = heightmapType;
        }
        
        public SpawnPlacementType getSpawnType() {
            return spawnType;
        }
        
        public Heightmap.Types getHeightmapType() {
            return heightmapType;
        }
        
        public void merge(SpawnPlacementStrategy strategy, SpawnPlacements.SpawnPredicate<T> predicate, @Nullable SpawnPlacementType spawnType, @Nullable Heightmap.Types heightmapType) {
            switch (strategy) {
                case AND -> andPredicates.add(predicate);
                case OR -> orPredicates.add(predicate);
                case REPLACE -> {
                    this.replacementPredicate = predicate;
                    if (spawnType != null) this.spawnType = spawnType;
                    if (heightmapType != null) this.heightmapType = heightmapType;
                }
            }
        }
        
        public SpawnPlacements.SpawnPredicate<T> build() {
            return Objects.requireNonNullElseGet(replacementPredicate, () -> (entityType, level, spawnType, pos, random) -> {
                boolean passesOr = originalPredicate.test(entityType, level, spawnType, pos, random);
                if (!passesOr) {
                    for (SpawnPlacements.SpawnPredicate<T> predicate : orPredicates) {
                        if (predicate.test(entityType, level, spawnType, pos, random)) {
                            passesOr = true;
                            break;
                        }
                    }
                }
                
                if (!passesOr) return false;
                
                for (SpawnPlacements.SpawnPredicate<T> predicate : andPredicates) {
                    if (!predicate.test(entityType, level, spawnType, pos, random)) {
                        return false;
                    }
                }
                
                return true;
            });
        }
    }
}
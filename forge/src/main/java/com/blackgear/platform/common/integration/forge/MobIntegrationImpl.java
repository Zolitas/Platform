package com.blackgear.platform.common.integration.forge;

import com.blackgear.platform.common.integration.MobIntegration;
import com.blackgear.platform.common.integration.MobInteraction;
import com.blackgear.platform.common.integration.v2.spawn_placement.SpawnPlacementStrategy;
import com.blackgear.platform.forge.CommonLoaderPipelines;
import com.blackgear.platform.forge.CommonModPipelines;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent.Operation;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MobIntegrationImpl {
    public static void registerIntegrations(Consumer<MobIntegration.Event> listener) {
        listener.accept(new MobIntegration.Event() {
            @Override
            public void registerMobInteraction(MobInteraction interaction) {
                CommonLoaderPipelines.MOB_INTERACTION.add(event -> {
                    InteractionResult result = interaction.onInteract(event.getEntity(), event.getTarget(), event.getHand());
                    if (result != InteractionResult.PASS) {
                        event.setCanceled(true);
                        event.setCancellationResult(result);
                    }
                });
            }
            
            @Override
            public void registerAttributes(Supplier<? extends EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> builder) {
                CommonModPipelines.MOB_ATTRIBUTE.add(event -> event.put(type.get(), builder.get().build()));
            }
            
            @Override
            public <T extends Mob> void registerPlacement(
                Supplier<EntityType<T>> entity,
                SpawnPlacements.Type spawnPlacement,
                Heightmap.Types heightmap,
                SpawnPlacements.SpawnPredicate<T> spawnPredicate,
                SpawnPlacementStrategy strategy
            ) {
                CommonModPipelines.SPAWN_PLACEMENT.add(event -> {
                    event.register(entity.get(), spawnPlacement, heightmap, spawnPredicate, Operation.valueOf(strategy.name()));
                });
            }
        });
    }
}
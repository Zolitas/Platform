package com.blackgear.platform.common.integration.neoforge;

import com.blackgear.platform.common.integration.BlockIntegration;
import com.blackgear.platform.common.integration.BlockInteraction;
import com.blackgear.platform.neoforge.CommonLoaderPipelines;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class BlockIntegrationImpl {
    public static final Object2FloatMap<ItemLike> COMPOSTABLES = new Object2FloatOpenHashMap<>();

    public static void registerIntegrations(Consumer<BlockIntegration.Event> listener) {
        listener.accept(new BlockIntegration.Event() {
            @Override
            public void registerBlockInteraction(BlockInteraction interaction) {
                CommonLoaderPipelines.BLOCK_INTERACTION.add(event -> {
                    InteractionResult result = interaction.onUse(new UseOnContext(event.getEntity(), event.getHand(), event.getHitVec()));
                    if (result != InteractionResult.PASS) {
                        event.setCanceled(true);
                        event.setCancellationResult(result);
                    }
                });
            }

            @Override
            public void registerFuelItem(ItemLike item, int burnTime) {
                CommonLoaderPipelines.FURNACE_FUEL.add(event -> {
                    if (event.getItemStack().is(item.asItem())) {
                        event.setBurnTime(burnTime);
                    }
                });
            }
            
            @Override
            public void registerFuelItem(TagKey<Item> tag, int burnTime) {
                CommonLoaderPipelines.FURNACE_FUEL.add(event -> {
                    if (event.getItemStack().is(tag)) {
                        event.setBurnTime(burnTime);
                    }
                });
            }
            
            @Override
            public void registerCompostableItem(ItemLike item, float chance) {
                COMPOSTABLES.putIfAbsent(item.asItem(), chance);
            }

            @Override
            public void registerOxidableBlock(Block less, Block more) {
                WeatheringCopper.NEXT_BY_BLOCK.get().put(less, more);
            }

            @Override
            public void registerWaxableBlock(Block unwaxed, Block waxed) {
                HoneycombItem.WAXABLES.get().put(unwaxed, waxed);
            }
        });
    }
}
package com.blackgear.platform.core.events.forge;

import com.blackgear.platform.core.events.ResourceReloadManager;
import com.blackgear.platform.forge.ClientModPipelines;
import com.blackgear.platform.forge.CommonLoaderPipelines;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

public class ResourceReloadManagerImpl {
    @OnlyIn(Dist.CLIENT)
    public static void registerClient(Consumer<ResourceReloadManager.ListenerEvent> exporter) {
        ClientModPipelines.RESOURCE_LISTENERS.add(event -> {
            exporter.accept((id, reloadListener) -> event.registerReloadListener(reloadListener));
        });
    }

    public static void registerServer(Consumer<ResourceReloadManager.ListenerEvent> exporter) {
        CommonLoaderPipelines.RESOURCE_LISTENERS.add(event -> {
            exporter.accept((id, reloadListener) -> event.addListener(reloadListener));
        });
    }
}
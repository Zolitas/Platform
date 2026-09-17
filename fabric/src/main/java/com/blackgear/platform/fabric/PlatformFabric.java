package com.blackgear.platform.fabric;

import com.blackgear.platform.Platform;
import com.blackgear.platform.common.v2.creative_tabs.CreativeTabIntegrations;
import com.blackgear.platform.common.v2.creative_tabs.CreativeTabIntegrationsImpl;
import com.blackgear.platform.core.Environment;
import com.blackgear.platform.core.events.fabric.ServerLifecycle;
import com.blackgear.platform.core.networking.Networking;
import com.blackgear.platform.core.networking.packet.ClientboundConfigSyncPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

public class PlatformFabric implements ModInitializer {
    private static MinecraftServer server;

    @Override
    public void onInitialize() {
        Platform.bootstrap();
        registerServerLifecycleEvents();

        if (Environment.isClientSide()) {
            ClientPipelines.bootstrap();
        }

        FabricCommonEvents.bootstrap();
        ServerLifecycle.bootstrap();
        
		Networking.register(registrar -> registrar.registerToClient(ClientboundConfigSyncPayload.TYPE, ClientboundConfigSyncPayload.STREAM_CODEC, ClientboundConfigSyncPayload::handler));
        
        CreativeTabIntegrations.setListener(CreativeTabIntegrationsImpl::register);
        CreativeTabIntegrationsImpl.bootstrap();
    }

    private void registerServerLifecycleEvents() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> PlatformFabric.server = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> PlatformFabric.server = null);
    }

    @Nullable
    public static MinecraftServer getServer() {
        return server;
    }
}
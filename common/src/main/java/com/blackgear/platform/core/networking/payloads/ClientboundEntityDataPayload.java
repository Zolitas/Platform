package com.blackgear.platform.core.networking.payloads;

import com.blackgear.platform.Platform;
import com.blackgear.platform.common.data.entity.SyncedDataHolder;
import com.blackgear.platform.common.data.entity.SyncedDataKey;
import com.blackgear.platform.core.networking.PayloadContext;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record ClientboundEntityDataPayload(int entityId, List<Entry<?>> entries) implements CustomPacketPayload {
    public static final Type<ClientboundEntityDataPayload> TYPE = new Type<>(Platform.resource("clientbound_entity_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundEntityDataPayload> STREAM_CODEC = StreamCodec.ofMember(ClientboundEntityDataPayload::write, ClientboundEntityDataPayload::decode);
    
    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeVarInt(this.entries.size());
        this.entries.forEach(entry -> entry.write(buf));
    }
    
    private static ClientboundEntityDataPayload decode(RegistryFriendlyByteBuf buf) {
        int entityId = buf.readVarInt();
        int count = buf.readVarInt();
        List<Entry<?>> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) entries.add(Entry.read(buf));
        return new ClientboundEntityDataPayload(entityId, entries);
    }
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(ClientboundEntityDataPayload payload, PayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) return;
            
            Level level = player.level();
            Entity entity = level.getEntity(payload.entityId());
            
            if (entity == null) return;
            payload.entries().forEach(entry -> applyEntryData(entity, entry));
        });
    }
    
    private static <T> void applyEntryData(Entity holder, Entry<T> entry) {
        SyncedDataHolder.get(holder).set(entry.key(), entry.value());
    }
    
    public record Entry<T>(SyncedDataKey<T> key, T value) {
        void write(RegistryFriendlyByteBuf buf) {
            buf.writeResourceLocation(this.key.id());
            this.key.codec().encode(buf, this.value);
        }
        
        @SuppressWarnings("unchecked")
        static Entry<?> read(RegistryFriendlyByteBuf buf) {
            ResourceLocation id = buf.readResourceLocation();
            SyncedDataKey<Object> key = (SyncedDataKey<Object>) SyncedDataKey.KEYS.get(id);
            if (key == null) throw new DecoderException("Unknown SyncedDataKey");
            Object value = key.codec().decode(buf);
            return new Entry<>(key, value);
        }
    }
}
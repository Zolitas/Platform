package com.blackgear.platform.core.network.packets;

import com.blackgear.platform.Platform;
import com.blackgear.platform.common.data.entity.SyncedDataHolder;
import com.blackgear.platform.common.data.entity.SyncedDataKey;
import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import io.netty.handler.codec.DecoderException;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public record ClientboundEntityDataPacket(int entityId, List<Entry<?>> entries) implements Packet<ClientboundEntityDataPacket> {
    public static final ResourceLocation ID = Platform.resource("clientbound_entity_data");
    public static final Handler HANDLER = new Handler();
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public PacketHandler<ClientboundEntityDataPacket> getHandler() {
        return HANDLER;
    }
    
    public static final class Handler implements PacketHandler<ClientboundEntityDataPacket> {
        @Override
        public void encode(ClientboundEntityDataPacket packet, FriendlyByteBuf buf) {
            buf.writeVarInt(packet.entityId());
            buf.writeVarInt(packet.entries().size());
            packet.entries().forEach(entry -> entry.write(buf));
        }
        
        @Override
        public ClientboundEntityDataPacket decode(FriendlyByteBuf buf) {
            int entityId = buf.readVarInt();
            int count = buf.readVarInt();
            List<Entry<?>> entries = new ArrayList<>(count);
            for (int i = 0; i < count; i++) entries.add(Entry.read(buf));
            return new ClientboundEntityDataPacket(entityId, entries);
        }
        
        @Override
        public PacketContext handle(ClientboundEntityDataPacket packet) {
            return (player, level) -> {
                Entity entity = level.getEntity(packet.entityId());
                if (entity == null) return;
                packet.entries().forEach(entry -> applyEntryData(entity, entry));
            };
        }
    }
    
    private static <T> void applyEntryData(Entity holder, Entry<T> entry) {
        SyncedDataHolder.get(holder).set(entry.key(), entry.value());
    }
    
    public record Entry<T>(SyncedDataKey<T> key, T value) {
        void write(FriendlyByteBuf buf) {
            buf.writeResourceLocation(this.key.id());
            buf.writeJsonWithCodec(this.key.codec(), this.value);
//            this.key.codec().encodeStart(NbtOps.INSTANCE, this.value);
        }
        
        @SuppressWarnings("unchecked")
        static Entry<?> read(FriendlyByteBuf buf) {
            ResourceLocation id = buf.readResourceLocation();
            SyncedDataKey<Object> key = (SyncedDataKey<Object>) SyncedDataKey.KEYS.get(id);
            if (key == null) throw new DecoderException("Unknown SyncedDataKey");
            Object value = buf.readJsonWithCodec(key.codec());
//            Object value = key.codec().simple();
            return new Entry<>(key, value);
        }
    }
}
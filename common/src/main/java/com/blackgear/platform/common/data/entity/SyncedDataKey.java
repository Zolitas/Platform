package com.blackgear.platform.common.data.entity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public record SyncedDataKey<T>(ResourceLocation id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, Supplier<T> fallback, SyncMode mode) {
    public static final Map<ResourceLocation, SyncedDataKey<?>> KEYS = new ConcurrentHashMap<>();
    
    public static <T> SyncedDataKey<T> create(ResourceLocation id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, Supplier<T> fallback, SyncMode mode) {
        SyncedDataKey<T> key = new SyncedDataKey<>(id, codec, fallback, mode);
        if (KEYS.putIfAbsent(id, key) != null) throw new IllegalStateException("Duplicate SynchedDataKey: " + id);
        return key;
    }
    
    public T getFallback() {
        return this.fallback.get();
    }
    
    public enum SyncMode {
        NONE(false, false),
        ALL(true, true),
        TRACKING_ONLY(true, false),
        SELF_ONLY(false, true);
        
        private final boolean tracking;
        private final boolean self;
        
        SyncMode(boolean tracking, boolean self) {
            this.tracking = tracking;
            this.self = self;
        }
        
        public boolean isTracking() {
            return this.tracking;
        }
        
        public boolean isSelf() {
            return this.self;
        }
    }
}
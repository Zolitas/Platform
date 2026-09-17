package com.blackgear.platform.core.api;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public final class RegistryKey<T> {
    private final ResourceLocation identifier;
    
    private RegistryKey(ResourceLocation identifier) {
        this.identifier = Objects.requireNonNull(identifier, "identifier must not be null");
    }
    
    public static <T> Codec<RegistryKey<T>> codec() {
        return ResourceLocation.CODEC.xmap(RegistryKey::of, RegistryKey::location);
    }
    
    public static <T> StreamCodec<ByteBuf, RegistryKey<T>> streamCodec() {
        return ResourceLocation.STREAM_CODEC.map(RegistryKey::of, RegistryKey::location);
    }
    
    public static <T> RegistryKey<T> of(ResourceLocation identifier) {
        return new RegistryKey<>(identifier);
    }
    
    public static <T> RegistryKey<T> of(String namespace, String path) {
        return of(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
    
    public ResourceLocation location() {
        return this.identifier;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof RegistryKey<?> other && this.identifier.equals(other.location());
    }
    
    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }
    
    @Override
    public String toString() {
        return "RegistryId[" + this.identifier + "]";
    }
}
package com.blackgear.platform.core;

import com.blackgear.platform.core.api.RegistryKey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class BuiltInCoreRegistry<T> {
    private final Map<ResourceLocation, T> codeDriven = new LinkedHashMap<>();
    private final Map<ResourceLocation, T> dataDriven = new LinkedHashMap<>();
    private final Map<T, ResourceLocation> reverseLookup = new LinkedHashMap<>();
    
    private final ResourceLocation identifier;
    private final String modId;
    private final @Nullable ResourceKey<? extends Registry<T>> key;
    
    @Deprecated(forRemoval = true)
    public static <T> BuiltInCoreRegistry<T> create(Registry<T> registry, String modId) {
        return new BuiltInCoreRegistry<>(registry.key(), modId);
    }
    
    @Deprecated(forRemoval = true)
    public static <T> BuiltInCoreRegistry<T> create(ResourceKey<? extends Registry<T>> registry, String modId) {
        return new BuiltInCoreRegistry<>(registry, modId);
    }
    
    @Deprecated(forRemoval = true)
    private BuiltInCoreRegistry(ResourceKey<? extends Registry<T>> key, String modId) {
        this(key, key.location(), modId);
    }
    
    public static <T> BuiltInCoreRegistry<T> create(ResourceLocation identifier, String modId) {
        return new BuiltInCoreRegistry<>(identifier, modId);
    }
    
    public BuiltInCoreRegistry(ResourceLocation identifier, String modId) {
        this(null, identifier, modId);
    }
    
    private BuiltInCoreRegistry(ResourceKey<? extends Registry<T>> key, ResourceLocation identifier, String modId) {
        this.modId = modId;
        this.identifier = identifier;
        this.key = key;
    }
    
    private T put(Map<ResourceLocation, T> target, ResourceLocation identifier, T value) {
        T previous = target.put(identifier, value);
        if (previous != null) this.reverseLookup.remove(previous);
        this.reverseLookup.put(value, identifier);
        return previous;
    }

    public T registerInternal(ResourceLocation identifier, T value) {
        return this.put(this.codeDriven, identifier, value);
    }

    public T registerInternal(String name, T value) {
        return this.registerInternal(ResourceLocation.fromNamespaceAndPath(this.modId, name), value);
    }
    
    public void registerDataDriven(ResourceLocation identifier, T value) {
        this.put(this.dataDriven, identifier, value);
    }
    
    public <E extends T> RegistryKey<T> register(String name, Function<RegistryKey<T>, E> value) {
        RegistryKey<T> key = RegistryKey.of(this.modId, name);
        this.registerInternal(key.location(), value.apply(key));
        return key;
    }
    
    public RegistryKey<T> register(String name, T value) {
        return this.register(name, key -> value);
    }
    
    @Deprecated(forRemoval = true)
    public <E extends T> ResourceKey<T> resource(String name, Function<ResourceKey<T>, E> value) {
        if (this.key == null) throw new IllegalArgumentException("BuiltInCoreRegistry: attempting to register a resource key without a registry");
        ResourceKey<T> key = ResourceKey.create(this.key, ResourceLocation.fromNamespaceAndPath(this.modId, name));
        this.registerInternal(key.location(), value.apply(key));
        return key;
    }
    
    public ResourceKey<T> resource(String name, T value) {
        return this.resource(name, key -> value);
    }
    
    public T get(ResourceLocation identifier) {
        T value = this.dataDriven.get(identifier);
        if (value == null) value = this.codeDriven.get(identifier);
        return value;
    }
    
    public T get(RegistryKey<T> key) {
        return this.get(key.location());
    }
    
    public T getOrDefault(ResourceLocation identifier, T fallback) {
        T value = this.get(identifier);
        return value == null ? fallback : value;
    }

    public T getOrThrow(RegistryKey<T> key) {
        T value = this.get(key);
        if (value == null) throw new IllegalStateException("Missing key in " + this.identifier.toString() + ": " + key);
        return value;
    }

    public ResourceLocation getKey(T value) {
        ResourceLocation identifier = this.reverseLookup.get(value);
        if (identifier == null) throw new IllegalArgumentException("Value not found in registry: " + value);
        return identifier;
    }
    
    public RegistryKey<T> getRegistryKey(T value) {
        return RegistryKey.of(this.getKey(value));
    }

    public boolean contains(ResourceLocation identifier) {
        return this.dataDriven.containsKey(identifier) || this.codeDriven.containsKey(identifier);
    }
    
    public boolean contains(RegistryKey<T> key) {
        return this.contains(key.location());
    }

    public T getRandomElement(Collection<T> collection, RandomSource random) {
        if (collection.isEmpty()) throw new IllegalArgumentException("Cannot get random element from empty collection");

        if (collection instanceof List<T> list) return list.get(random.nextInt(list.size()));
        
        int index = random.nextInt(collection.size());
        Iterator<T> iterator = collection.iterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }

        return iterator.next();
    }

    public T getRandomElement(RandomSource random) {
        Map<ResourceLocation, T> allEntries = this.allEntries();
        if (allEntries.isEmpty()) throw new IllegalStateException("Registry is empty");
        return this.getRandomElement(allEntries.values(), random);
    }
    
    private Map<ResourceLocation, T> allEntries() {
        Map<ResourceLocation, T> entries = new LinkedHashMap<>(this.codeDriven);
        entries.putAll(this.dataDriven);
        return entries;
    }

    public Collection<T> values() {
        return List.copyOf(this.allEntries().values());
    }
    
    public Set<ResourceLocation> keys() {
        return Set.copyOf(this.allEntries().keySet());
    }

    public Map<ResourceLocation, T> entries() {
        return Map.copyOf(this.allEntries());
    }
    
    public void clearDataDrivenEntries() {
        this.dataDriven.values().forEach(this.reverseLookup::remove);
        this.dataDriven.clear();
    }

    public void register() { /* NO-OP */ }
}
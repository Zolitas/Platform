package com.blackgear.platform.forge;

import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventPipeline<T> {
    private final List<Consumer<T>> consumers = new ArrayList<>();
    
    public void add(Consumer<T> consumer) {
        this.consumers.add(consumer);
    }
    
    public void dispatch(T event) {
        for (Consumer<T> consumer : consumers) {
            consumer.accept(event);
        }
    }
    
    public void dispatchCancelable(T event) {
        if (event instanceof Event cancelable) {
            for (Consumer<T> consumer : consumers) {
                consumer.accept(event);
                if (cancelable.isCanceled()) break;
            }
        } else {
            this.dispatch(event);
        }
    }
}
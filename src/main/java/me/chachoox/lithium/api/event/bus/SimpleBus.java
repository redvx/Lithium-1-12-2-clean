/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.event.bus;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import me.chachoox.lithium.api.event.bus.api.EventBus;
import me.chachoox.lithium.api.event.bus.api.IListener;
import me.chachoox.lithium.api.event.bus.api.Subscriber;
import me.chachoox.lithium.api.interfaces.Minecraftable;

public final class SimpleBus
implements EventBus,
Minecraftable {
    private final Map<Class<?>, List<IListener>> listeners = new ConcurrentHashMap();
    private final Set<Subscriber> subscribers = Collections.newSetFromMap(new ConcurrentHashMap());
    private final Set<IListener> subbedlisteners = Collections.newSetFromMap(new ConcurrentHashMap());

    @Override
    public void dispatch(Object object) {
        List<IListener> listening = this.listeners.get(object.getClass());
        if (listening != null) {
            for (IListener listener : listening) {
                listener.call(object);
            }
        }
    }

    @Override
    public void dispatch(Object object, Class<?> type) {
        List<IListener> listening = this.listeners.get(object.getClass());
        if (listening != null) {
            for (IListener listener : listening) {
                if (listener.getType() != null && listener.getType() != type) continue;
                listener.call(object);
            }
        }
    }

    @Override
    public void register(IListener<?> listener) {
        if (this.subbedlisteners.add(listener)) {
            this.addAtPriority(listener, this.listeners.computeIfAbsent(listener.getTarget(), v -> new CopyOnWriteArrayList()));
        }
    }

    public void unregister(IListener listener) {
        List<IListener> list;
        if (this.subbedlisteners.remove(listener) && (list = this.listeners.get(listener.getTarget())) != null) {
            list.remove(listener);
        }
    }

    @Override
    public void dispatchReversed(Object object, Class<?> type) {
        List<IListener> list = this.listeners.get(object.getClass());
        if (list != null) {
            ListIterator<IListener> li = list.listIterator(list.size());
            while (li.hasPrevious()) {
                IListener l = li.previous();
                if (l == null || l.getType() != null && l.getType() != type) continue;
                l.call(object);
            }
        }
    }

    @Override
    public void subscribe(Object object) {
        if (object instanceof Subscriber) {
            Subscriber subscriber = (Subscriber)object;
            for (IListener iListener : subscriber.getListeners()) {
                this.register(iListener);
            }
            this.subscribers.add(subscriber);
        }
    }

    @Override
    public void unsubscribe(Object object) {
        if (object instanceof Subscriber) {
            Subscriber subscriber = (Subscriber)object;
            for (IListener iListener : subscriber.getListeners()) {
                this.unregister(iListener);
            }
            this.subscribers.remove(subscriber);
        }
    }

    @Override
    public boolean isSubscribed(Object object) {
        if (object instanceof Subscriber) {
            return this.subscribers.contains(object);
        }
        if (object instanceof IListener) {
            return this.subbedlisteners.contains(object);
        }
        return false;
    }

    @Override
    public boolean hasSubscribers(Class<?> clazz) {
        List<IListener> listening = this.listeners.get(clazz);
        return listening != null && !listening.isEmpty();
    }

    @Override
    public boolean hasSubscribers(Class<?> clazz, Class<?> type) {
        List<IListener> listening = this.listeners.get(clazz);
        return listening != null && listening.stream().anyMatch(listener -> listener.getType() == null || listener.getType() == type);
    }

    private void addAtPriority(IListener<?> listener, List<IListener> list) {
        int index;
        for (index = 0; index < list.size() && listener.getPriority() < list.get(index).getPriority(); ++index) {
        }
        list.add(index, listener);
    }
}


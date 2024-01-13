/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.observable;

import java.util.LinkedList;
import java.util.List;
import me.chachoox.lithium.api.observable.Observer;

public class Observable<T> {
    private final List<Observer<? super T>> observers = new LinkedList<Observer<? super T>>();

    public T onChange(T value) {
        for (Observer<T> observer : this.observers) {
            observer.onChange(value);
        }
        return value;
    }

    public void addObserver(Observer<? super T> observer) {
        if (observer != null && !this.observers.contains(observer)) {
            this.observers.add(observer);
        }
    }

    public void removeObserver(Observer<? super T> observer) {
        this.observers.remove(observer);
    }
}


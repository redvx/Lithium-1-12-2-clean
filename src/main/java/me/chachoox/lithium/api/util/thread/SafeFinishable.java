/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.thread;

import java.util.concurrent.atomic.AtomicBoolean;
import me.chachoox.lithium.api.util.thread.Finishable;
import me.chachoox.lithium.api.util.thread.SafeRunnable;

public abstract class SafeFinishable
extends Finishable
implements SafeRunnable {
    public SafeFinishable() {
        this(new AtomicBoolean());
    }

    public SafeFinishable(AtomicBoolean finished) {
        super(finished);
    }

    @Override
    public void run() {
        try {
            this.runSafely();
        }
        catch (Throwable t) {
            this.handle(t);
        }
        finally {
            this.setFinished(true);
        }
    }

    @Override
    @Deprecated
    protected void execute() {
    }
}


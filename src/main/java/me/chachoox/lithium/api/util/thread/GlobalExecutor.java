/*
 * Decompiled with CFR 0.152.
 */
package me.chachoox.lithium.api.util.thread;

import java.util.concurrent.ExecutorService;
import me.chachoox.lithium.api.util.thread.ThreadUtil;

public interface GlobalExecutor {
    public static final ExecutorService EXECUTOR = ThreadUtil.newDaemonCachedThreadPool();
    public static final ExecutorService FIXED_EXECUTOR = ThreadUtil.newFixedThreadPool((int)((double)Runtime.getRuntime().availableProcessors() / 1.5));
}


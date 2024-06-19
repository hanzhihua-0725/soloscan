package org.soloquest.soloscan;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Function;

public class SoloscanCache<T, R> {

    private final ConcurrentHashMap<T, FutureTask<R>> map = new ConcurrentHashMap<>();
    private final Function<T, R> function;
    private final boolean cached;

    public SoloscanCache(Function<T, R> function) {
        this(false, function);
    }

    public SoloscanCache(boolean cached, Function<T, R> function) {
        this.cached = cached;
        this.function = function;
    }

    public R getR(T t) throws ExecutionException, InterruptedException {
        if (cached) {
            FutureTask<R> existTask = map.get(t);
            if (existTask != null) {
                return existTask.get();
            } else {
                FutureTask<R> task = new FutureTask<>(new Callable<R>() {
                    @Override
                    public R call() throws Exception {
                        return function.apply(t);
                    }
                });
                existTask = map.putIfAbsent(t, task);
                if (existTask != null) {
                    return existTask.get();
                } else {
                    task.run();
                    return task.get();
                }
            }
        } else {
            return function.apply(t);
        }

    }

    public void clear() {
        this.map.clear();
    }

}

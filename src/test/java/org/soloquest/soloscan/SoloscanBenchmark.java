package org.soloquest.soloscan;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.soloquest.soloscan.dataset.ListDataSet;
import org.soloquest.soloscan.runtime.lang.Numbers;
import org.soloquest.soloscan.utils.Preconditions;

import java.io.IOException;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Slf4j
public class SoloscanBenchmark {

    static List<Map<String, Object>> data;

    @Setup(org.openjdk.jmh.annotations.Level.Trial)
    public synchronized void prepareData() throws IOException {
        if (data != null) {
            System.out.println("use the existing data");
            return;
        }

    }

    @Setup
    public void setup() throws IOException {
        printJVM();
    }

    @TearDown
    public void tearDown() {
        printJVM();
    }

    @Benchmark
    @Warmup(iterations = 0, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 50, time = 1, timeUnit = TimeUnit.SECONDS)
    @Threads(10)
    @Fork(1)
    public void execute() {
        SoloscanExecutorExt instance = SoloscanExecutorExt.INSTANCE;
        List<String> list = new ArrayList<>();
        list.add("{count(SCCC)}");
        list.add("{count(SCCC),grouping(SCCC,RQ),SCCC=20}");
        list.add("{count(SCCC),grouping(SCCC),SCCC=20}");
        list.add("{count(SCCC!=20)/count()}");
        list.add("{count(SCCC),SCCC,SCCC=5||SCCC=11||SCCC=20}");
        Map<String, Object> map = (Map<String, Object>) instance.executeList(list, new ListDataSet<>(data));
        map = new TreeMap<>(map);
        Preconditions.checkArgument(map.size() == 5, "map size:" + map.size() + "," + map);
        Iterator iterator = map.values().iterator();
        Object obj1 = iterator.next();
        Preconditions.checkArgument(obj1 instanceof Number, "");
        Object obj2 = iterator.next();
        Preconditions.checkArgument(obj2 instanceof Map, "");
        Object obj3 = iterator.next();
        Preconditions.checkArgument(obj3 instanceof Map, "");
        Object obj4 = iterator.next();
        Preconditions.checkArgument(obj4 instanceof Number, "");
        Object obj5 = iterator.next();
        Preconditions.checkArgument(obj5 instanceof Map, "");
        Preconditions.checkArgument(Numbers.gt(obj1, obj4), "");

    }

    private void printJVM() {
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        long totalLoadedClassCount = classLoadingMXBean.getTotalLoadedClassCount();
        int loadedClassCount = classLoadingMXBean.getLoadedClassCount();
        long unloadedClassCount = classLoadingMXBean.getUnloadedClassCount();

        System.out.println("Total Loaded Class Count: " + totalLoadedClassCount);
        System.out.println("Currently Loaded Class Count: " + loadedClassCount);
        System.out.println("Total Unloaded Class Count: " + unloadedClassCount);
    }

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(SoloscanBenchmark.class.getSimpleName())
                .build();

        Collection<RunResult> results = new Runner(options).run();
        System.out.println(results);
    }

}

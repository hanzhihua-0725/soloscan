package org.soloquest.soloscan;

import lombok.extern.slf4j.Slf4j;
import org.soloquest.soloscan.dataset.DataSet;
import org.soloquest.soloscan.dataset.Row;
import org.soloquest.soloscan.dataset.TerminalRow;
import org.soloquest.soloscan.utils.Preconditions;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
class DataProvider implements Runnable {

    private final static AtomicLong FLAG = new AtomicLong();
    public final DataSet dataSet;
    public final Expression[] expressions;

    private DataProvider(DataSet dataSet, Expression... expressions) {
        this.dataSet = dataSet;
        this.expressions = expressions;
        Preconditions.checkNotNull(dataSet);
        Preconditions.checkNotNull(expressions);
    }

    public static void work(DataSet dataSet, Expression... expressions) {
        log.warn("dataprovider size:{}, expressions:{}", expressions.length, expressions);
        DataProvider dataProvider = new DataProvider(dataSet, expressions);
        Thread thread = new Thread(dataProvider, "DataProvider_thread_" + FLAG.getAndIncrement());
        thread.setDaemon(true);
        thread.setUncaughtExceptionHandler((t, e) -> log.error("data provider occurs error", e));
        thread.start();
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        int rowNum = 0;
        while (dataSet.hasNext()) {
            Row row = dataSet.next();
            rowNum++;
            for (Expression expression : expressions) {
                expression.consumeRow(row);
            }
            if (rowNum % 10000 == 0) {
                Thread.yield();
            }
        }
        for (Expression expression : expressions) {
            expression.consumeRow(TerminalRow.INSTANCE);
        }
        long cost = System.currentTimeMillis() - start;
        if (cost > 3000) {
            log.error("Slow feed,data provider feed {} record, cost : {} ms", rowNum, cost);
        } else if (cost > 1000) {
            log.warn("Slow feed, data provider feed {} record, cost : {} ms", rowNum, cost);
        }
    }

}

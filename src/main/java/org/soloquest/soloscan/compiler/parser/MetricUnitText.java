package org.soloquest.soloscan.compiler.parser;

import java.util.concurrent.atomic.AtomicInteger;

public class MetricUnitText {

    private static AtomicInteger INIT_PLACEHOLDER = new AtomicInteger((int) ('A'));
    private final String placeHolder;
    private final String text;

    public MetricUnitText(String text) {
        this.text = text;
        this.placeHolder = "MU_" + (char) INIT_PLACEHOLDER.getAndIncrement();
    }
}

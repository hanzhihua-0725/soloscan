package org.soloquest.soloscan.compiler.parser;

import lombok.Data;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class AggFunctionText {

    private static AtomicInteger INIT_PLACEHOLDER = new AtomicInteger();
    private final String name;
    private final String placeHolder;

    private String innerString;

    public AggFunctionText(String name) {
        this.name = name;
        this.placeHolder = ("PH_AGG_" + INIT_PLACEHOLDER.getAndIncrement()).toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggFunctionText that = (AggFunctionText) o;
        return Objects.equals(name, that.name) && Objects.equals(innerString, that.innerString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, innerString);
    }


}

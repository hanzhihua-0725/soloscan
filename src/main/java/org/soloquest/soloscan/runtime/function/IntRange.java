package org.soloquest.soloscan.runtime.function;

public class IntRange {

    private int start;
    private int end;
    private int step;

    public IntRange(int start, int end) {
        this(start, end, start <= end ? 1 : -1);
    }

    public IntRange(int start, int end, int step) {
        if (start <= end && step < 0) {
            throw new IllegalArgumentException("Invalid step for the given range.");
        } else if (start > end && step > 0) {
            throw new IllegalArgumentException("Invalid step for the given range.");
        }
        this.start = start;
        this.end = end;
        this.step = step;
    }

    public boolean in(int num) {
        if (step > 0) {
            return num >= start && num < end && (num - start) % step == 0;
        } else {
            return num <= start && num > end && (start - num) % (-step) == 0;
        }
    }


}

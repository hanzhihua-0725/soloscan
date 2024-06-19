package org.soloquest.soloscan.utils;

import java.util.ArrayList;
import java.util.List;

public class Preconditions {

    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static void checkNotNullOrEmpty(String str, String errorMessage) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static <T> void batchCheckNotNull(T... references) {
        int i = 1;
        List<Integer> nullList = new ArrayList<>();
        for (T reference : references) {
            if (reference == null) {
                nullList.add(i++);
            }
        }
        if (nullList.size() > 0) {
            throw new NullPointerException(String.format("arg %s is null", nullList));

        }
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }


}

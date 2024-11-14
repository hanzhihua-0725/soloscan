package org.soloquest.soloscan.utils;


import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MetricUtilsTest {

    @Test
    public void testSlideAgg() {
        // Setup test data
        Map<String, Number> data = new HashMap<>();
        data.put("1", 10);
        data.put("2", 20);
        data.put("3", 30);
        data.put("4", 40);
        data.put("5", 50);
        data.put("6", 60);

        data.put("10", 10);
        data.put("20", 10);
        data.put("100", 10);
        data.put("21", 10);

        Map<String, Number> result = MetricUtils.slideAgg(data, 4);
        assertEquals(7, result.size());
        assertEquals(100, result.get("1___4").intValue());
        assertEquals(140, result.get("2___5").intValue());
        assertEquals(130, result.get("5___20").intValue());
        assertEquals(90, result.get("6___21").intValue());

        result = MetricUtils.slideAgg(data, 4,4);
        assertEquals(3, result.size());
        assertEquals(100, result.get("1___4").intValue());
        assertEquals(130, result.get("5___20").intValue());
        assertEquals(20, result.get("21___100").intValue());
    }

    @Test
    public void testSlideAggWithLessThanWindowSize() {
        Map<String, Number> data = new HashMap<>();
        data.put("1", 10);
        data.put("2", 20);
        Map<String, Number> result = MetricUtils.slideAgg(data, 4);
        assertEquals(1, result.size());
    }

    @Test
    public void testSlideAggWithEmptyData() {
        // Setup test data as empty
        Map<String, Number> data = new HashMap<>();

        // Execute the slideAgg method
        Map<String, Number> result = MetricUtils.slideAgg(data, 4);

        // Assert the expected results - should return an empty map
        assertEquals(0, result.size());
    }

    @Test
    public void testSlideAggWithWindowSizeEqualToDataSize() {
        Map<String, Number> data = new HashMap<>();
        data.put("1", 10);
        data.put("2", 20);
        data.put("3", 30);

        Map<String, Number> result = MetricUtils.slideAgg(data, 3);

        assertEquals(1, result.size());
        assertEquals(60, result.get("1___3").intValue());
    }
}

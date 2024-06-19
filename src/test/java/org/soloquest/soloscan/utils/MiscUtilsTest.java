package org.soloquest.soloscan.utils;


import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MiscUtilsTest {

    private Map<String, String> expressionMap;
    private Map<String, String> placeHolderMap;

    @Before
    public void setUp() {
        expressionMap = new HashMap<>();
        placeHolderMap = new HashMap<>();
    }

    @Test
    public void testApplyPlaceHolderWithNoPlaceHolders() {
        expressionMap.put("key1", "value1");
        MiscUtils.applyPlaceHolder(expressionMap, placeHolderMap);
        assertEquals("value1", expressionMap.get("key1"));
    }

    @Test
    public void testApplyPlaceHolderWithPlaceHolders() {
        expressionMap.put("key1", "Hello, {{name}}!");
        placeHolderMap.put("name", "World");
        MiscUtils.applyPlaceHolder(expressionMap, placeHolderMap);
        assertEquals("Hello, World!", expressionMap.get("key1"));
    }

    @Test
    public void testApplyPlaceHolderWithPlaceHolders2() {
        expressionMap.put("key1", "Hello, {{name}}!xxx");
        expressionMap.put("key2", "Hello, xxx{{name}}!xxx");
        placeHolderMap.put("name", "World");
        MiscUtils.applyPlaceHolder(expressionMap, placeHolderMap);
        assertEquals("Hello, World!xxx", expressionMap.get("key1"));
        assertEquals("Hello, xxxWorld!xxx", expressionMap.get("key2"));
    }

    @Test
    public void testApplyPlaceHolderWithMissingPlaceHolder() {
        expressionMap.put("key1", "Hello, {{missing}}!");
        placeHolderMap.put("name", "World");
        MiscUtils.applyPlaceHolder(expressionMap, placeHolderMap);
        assertEquals("Hello, {{missing}}!", expressionMap.get("key1"));
    }

    @Test
    public void testApplyPlaceHolderWithNullValues() {
        expressionMap.put("key1", null);
        MiscUtils.applyPlaceHolder(expressionMap, placeHolderMap);
        assertEquals(null, expressionMap.get("key1"));
    }

    @Test
    public void testApplyPlaceHolderWithEmptyPlaceHolderMap() {
        expressionMap.put("key1", "Hello, {{name}}!");
        MiscUtils.applyPlaceHolder(expressionMap, new HashMap<>());
        assertEquals("Hello, {{name}}!", expressionMap.get("key1"));
    }

    @Test
    public void testApplyPlaceHolderWithNullPlaceHolderMap() {
        expressionMap.put("key1", "Hello, {{name}}!");
        MiscUtils.applyPlaceHolder(expressionMap, null);
        assertEquals("Hello, {{name}}!", expressionMap.get("key1"));
    }
}

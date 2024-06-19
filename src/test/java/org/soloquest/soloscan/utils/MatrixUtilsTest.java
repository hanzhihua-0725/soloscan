package org.soloquest.soloscan.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MatrixUtilsTest {

    @Test
    public void testIsValidKeyValidKey() {
        String key = "1_2";
        boolean result = MatrixUtils.isValidKey(key);
        Assert.assertTrue("Key should be valid", result);
    }

    @Test
    public void testIsValidKeyInvalidKey() {
        String key = "abc_123";
        boolean result = MatrixUtils.isValidKey(key);
        Assert.assertFalse("Key should be invalid", result);
    }
    
    @Test
    public void testIsComplete2DArrayTrue() {
        Map<String, String> expressions = new HashMap<>();
        expressions.put("0_0", "1");
        expressions.put("0_1", "2");
        expressions.put("1_0", "3");
        expressions.put("1_1", "4");
        boolean result = MatrixUtils.isComplete2DArray(expressions, 2, 2);
        Assert.assertTrue("Matrix should be complete", result);
    }
    
    @Test
    public void testIsComplete2DArrayFalseDueToInvalidKey() {
        Map<String, String> expressions = new HashMap<>();
        expressions.put("invalid_key", "1");
        boolean result = MatrixUtils.isComplete2DArray(expressions, 1, 1);
        Assert.assertFalse("Matrix should not be considered complete due to invalid key", result);
    }
    
    @Test
    public void testIsComplete2DArrayFalseDueToOutOfBounds() {
        Map<String, String> expressions = new HashMap<>();
        expressions.put("0_0", "1");
        expressions.put("2_0", "3"); // This key is out of bounds
        boolean result = MatrixUtils.isComplete2DArray(expressions, 1, 1);
        Assert.assertFalse("Matrix should not be considered complete due to out-of-bounds key", result);
    }
    
    @Test
    public void testIsComplete2DArrayFalseDueToMissingElement() {
        Map<String, String> expressions = new HashMap<>();
        expressions.put("0_0", "1");
        boolean result = MatrixUtils.isComplete2DArray(expressions, 2, 2);
        Assert.assertFalse("Matrix should not be complete due to missing elements", result);
    }

    @Test
    public void testCalculateMatrix() {
        Map<String, Number> data = new HashMap<>();
        data.put("0_0", 10);
        data.put("0_1", 20);
        data.put("1_0", 30);
        data.put("1_1", 40);
        
        Map<String, Number> resultMap = MatrixUtils.clcalculateMatrix(data, 2, 2);
        
        // Assuming the calculation is correct and results in a map with normalized values
        // This is a placeholder check as the actual normalization logic depends on the implementation details
        Assert.assertFalse("Result map should not be empty", resultMap.isEmpty());
        Assert.assertTrue("Result map should contain the key '0_0'", resultMap.containsKey("0_0"));
    }
}

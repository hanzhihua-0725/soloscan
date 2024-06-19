package org.soloquest.soloscan.utils;

import java.util.HashMap;
import java.util.Map;

public class MatrixUtils {

    public static boolean isValidKey(String key) {
        return key.matches("\\d+_\\d+");
    }

    public static boolean isComplete2DArray(Map<String, String> expressions, int rows, int columns) {
        boolean[][] matrix = new boolean[rows][columns];
        for (String key : expressions.keySet()) {
            if (!isValidKey(key)) {
                return false; // Invalid key format
            }
            String[] parts = key.split("_");
            int row = Integer.parseInt(parts[0]);
            int column = Integer.parseInt(parts[1]);
            if (row >= rows || column >= columns) {
                return false; // Out of bounds
            }
            matrix[row][column] = true;
        }
        // Check if every element in the matrix is true
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!matrix[i][j]) {
                    return false; // Missing element
                }
            }
        }
        return true;
    }

    public static Map<String,Number> clcalculateMatrix(Map<String, Number> data,int rows, int columns){

        double[][] matrix = new double[rows][columns];
        for (Map.Entry<String, Number> entry : data.entrySet()) {
            String key = entry.getKey();
            Number value = entry.getValue();
            String[] parts = key.split("_");
            int row = Integer.parseInt(parts[0]);
            int column = Integer.parseInt(parts[1]);
            matrix[row][column] = value.doubleValue();
        }

        double[] rowAverages = new double[rows];
        for (int i = 0; i < rows; i++) {
            double sum = 0;
            for (int j = 0; j < columns; j++) {
                sum += matrix[i][j];
            }
            rowAverages[i] = sum / columns;
        }

        double[] columnAverages = new double[columns];
        for (int j = 0; j < columns; j++) {
            double sum = 0;
            for (int i = 0; i < rows; i++) {
                sum += matrix[i][j];
            }
            columnAverages[j] = sum / rows;
        }

        double sumOfAverages = 0;
        for (int i = 0; i < rows; i++) {
            sumOfAverages += rowAverages[i];
        }
        for (int j = 0; j < columns; j++) {
            sumOfAverages += columnAverages[j];
        }
        double overallAverage = sumOfAverages / (rows + columns);

        Map<String, Number> resultMap = new HashMap<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double newValue = matrix[i][j] - rowAverages[i] - columnAverages[j] + overallAverage;
                String key = i + "_" + j;
                resultMap.put(key, newValue);
            }
        }
        return resultMap;

    }
}

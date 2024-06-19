package org.soloquest.soloscan.dataset;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JDBCDataSet implements DataSet {
    private final ResultSet rs;
    private ResultSetMetaData rsmd;
    private int columns;

    private final Map<String, Function<Row, Object>> calcColumnMap = new HashMap<>();

    public JDBCDataSet(ResultSet rs) {
        this.rs = rs;
        try {
            this.rsmd = rs.getMetaData();
            int columns = rs.getMetaData().getColumnCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasNext() {
        try {
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Row next() {
        Map<String, Object> row = new HashMap<String, Object>(columns);
        for (int i = 1; i <= columns; ++i) {
            try {
                row.put(rsmd.getColumnName(i), rs.getObject(i));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        Row result = new MapRow(row);
        if (calcColumnMap.size() > 0) {
            calcColumnMap.entrySet().stream().map(e -> {
                return result.putValue(e.getKey(), e.getValue().apply(result));
            });
        }
        return result;
    }

    @Override
    public boolean addCalcColumn(String columnName, Function<Row, Object> function) {
        return calcColumnMap.putIfAbsent(columnName, function) == null;
    }

    @Override
    public void close() {
        try {
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

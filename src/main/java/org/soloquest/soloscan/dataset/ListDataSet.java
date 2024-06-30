package org.soloquest.soloscan.dataset;

import org.soloquest.soloscan.SoloscanOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ListDataSet<T extends Map> implements DataSet {

    private final List<T> dataSet;
    private final Map<String, Function<Row, Object>> calcColumnMap = new HashMap<>();
    private Iterator<T> iterator;

    public ListDataSet(List<T> dataSet) {
        this.dataSet = dataSet;
        iterator = this.dataSet.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Row next() {
        T object = iterator.next();

        Row row = new MapRow(object);
        if (calcColumnMap.size() > 0) {
            calcColumnMap.entrySet().stream().forEach(e -> {
                row.putValue(e.getKey(), e.getValue().apply(row));
            });
        }
        return row;
    }

    @Override
    public boolean addCalcColumn(String columnName, Function<Row, Object> function) {
        if (SoloscanOptions.getOption(SoloscanOptions.COLUMN_CASE_INSENSITIVE)) {
            return calcColumnMap.putIfAbsent(columnName.toLowerCase(), function) == null;
        } else {
            return calcColumnMap.putIfAbsent(columnName, function) == null;
        }
    }


    @Override
    public void close() {
        dataSet.clear();
    }
}

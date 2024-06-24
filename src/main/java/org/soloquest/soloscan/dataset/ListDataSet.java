package org.soloquest.soloscan.dataset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ListDataSet<T extends Map> implements DataSet {

    private final List<T> dataSet;
    private Iterator<T> iterator;

    private final Map<String, Function<Row, Object>> calcColumnMap = new HashMap<>();

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
                row.putValue(e.getKey().toLowerCase(), e.getValue().apply(row));
            });
        }
        return row;
    }

    @Override
    public boolean addCalcColumn(String columnName, Function<Row, Object> function) {
        return calcColumnMap.putIfAbsent(columnName.toLowerCase(), function) == null;
    }


    @Override
    public void close() {
        dataSet.clear();
    }
}

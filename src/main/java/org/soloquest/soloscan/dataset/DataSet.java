package org.soloquest.soloscan.dataset;

import java.util.function.Function;

public interface DataSet extends Cloneable {

    DataSet EMPTY = new DataSet() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Row next() {
            return null;
        }

        @Override
        public boolean addCalcColumn(String columnName, Function<Row, Object> function) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() {

        }
    };

    boolean hasNext();

    Row next();

    boolean addCalcColumn(String columnName, Function<Row, Object> function);

    void close();

    default DataSet newDataSet() {
        return null;
    }
}

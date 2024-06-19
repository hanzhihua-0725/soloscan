package org.soloquest.soloscan.dataset;

public interface Row {

    Object getValue(String column);

    public boolean putValue(String column, Object value);

    boolean containsColumn(String column);
}

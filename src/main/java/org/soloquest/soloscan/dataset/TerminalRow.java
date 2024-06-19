package org.soloquest.soloscan.dataset;

public class TerminalRow implements Row {

    public final static TerminalRow INSTANCE = new TerminalRow();

    private TerminalRow() {
    }

    @Override
    public Object getValue(String column) {
        return null;
    }

    @Override
    public boolean putValue(String column, Object value) {
        return false;
    }

    @Override
    public boolean containsColumn(String column) {
        return false;
    }
}

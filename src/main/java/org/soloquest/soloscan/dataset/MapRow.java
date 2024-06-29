package org.soloquest.soloscan.dataset;

import org.soloquest.soloscan.SoloscanOptions;
import org.soloquest.soloscan.utils.MiscUtils;

import java.util.Map;

public class MapRow implements Row {

    private final Map<String, Object> map;

    public MapRow(Map<String, Object> map) {
        if (SoloscanOptions.getOption(SoloscanOptions.COLUMN_CASE_INSENSITIVE)) {
            this.map = MiscUtils.toLowerKey(map);
        } else {
            this.map = map;
        }

    }

    @Override
    public Object getValue(String column) {
        return map.get(column);
    }

    @Override
    public boolean putValue(String column, Object value) {
        return map.putIfAbsent(column, value) == null;
    }

    @Override
    public boolean containsColumn(String column) {
        return map.containsKey(column);
    }

    @Override
    public String toString() {
        return "MapRow{" +
                "map=" + map +
                '}';
    }
}

package org.soloquest.soloscan;

import org.soloquest.soloscan.dataset.Row;

import java.util.Map;

public interface Expression {

    Object execute(Map<String, Object> map);

    boolean consumeRow(Row row);

}

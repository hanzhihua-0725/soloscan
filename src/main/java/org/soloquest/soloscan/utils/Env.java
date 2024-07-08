package org.soloquest.soloscan.utils;

import org.soloquest.soloscan.Expression;
import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.dataset.Row;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Env implements Map<String, Object> {

    public static final Map EMPTY_ENV = new HashMap();
    public static final String DATASET_KEY = "dateset_key";
    private final Map<String, Object> envMap;
    private final SoloscanExecutor instance;
    private final Expression expression;
    private Row dataMap;
    private boolean canPutAggrValue = true;

    public Env(SoloscanExecutor instance, Expression expression, Map<String, Object> envMap) {
        this.instance = instance;
        this.expression = expression;
        this.envMap = envMap;
    }

    public Env newRowEnv(Row row) {
        Env env = new Env(this.instance, this.expression, this.envMap);
        env.dataMap = row;
        env.canPutAggrValue = false;
        return env;
    }

    public Map<String, Object> getEnvMap() {
        return envMap;
    }

    public Expression getExpression() {
        return expression;
    }

    public SoloscanExecutor getInstance() {
        return this.instance;
    }

    public void putAggrValue(String key, Object value) {
        if (canPutAggrValue) {
            envMap.put(key, value);
        } else {
            throw new UnsupportedOperationException("this env can not put aggfunction value");
        }
    }

    @Override
    public int size() {
        return this.envMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.envMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (dataMap != null) {
            return dataMap.containsColumn(key.toString());
        }
        return this.envMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.envMap.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        if (key == null) {
            return null;
        }
        if (dataMap != null && dataMap.containsColumn(key.toString())) {
            return dataMap.getValue(key.toString());
        }
        return this.envMap.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return this.envMap.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return this.envMap.remove(key);
    }

    @Override
    public void putAll(Map m) {
        this.envMap.putAll(m);
    }

    @Override
    public void clear() {
        this.envMap.clear();
    }

    @Override
    public Set keySet() {
        return this.envMap.keySet();
    }

    @Override
    public Collection values() {
        return this.envMap.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return this.envMap.entrySet();
    }
}

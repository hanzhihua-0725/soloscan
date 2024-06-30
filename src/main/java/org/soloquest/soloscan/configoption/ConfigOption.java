package org.soloquest.soloscan.configoption;

import org.soloquest.soloscan.utils.Preconditions;

public class ConfigOption<T> {

    static final String EMPTY_DESCRIPTION = "";
    private final String key;
    private final T defaultValue;
    private final String description;
    private final Class<?> clazz;
    private final boolean isList;

    ConfigOption(
            String key,
            Class<?> clazz,
            String description,
            T defaultValue,
            boolean isList) {
        this.key = Preconditions.checkNotNull(key);
        this.description = description;
        this.defaultValue = defaultValue;
        this.clazz = Preconditions.checkNotNull(clazz);
        this.isList = isList;
    }

    Class<?> getClazz() {
        return clazz;
    }

    boolean isList() {
        return isList;
    }

    public String key() {
        return key;
    }

    public boolean hasDefaultValue() {
        return defaultValue != null;
    }

    public T defaultValue() {
        return defaultValue;
    }

    public ConfigOption<T> withDescription(final String description) {
        return new ConfigOption<>(key, clazz, description, defaultValue, isList);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && o.getClass() == ConfigOption.class) {
            ConfigOption<?> that = (ConfigOption<?>) o;
            return this.key.equals(that.key)
                    && (this.defaultValue == null
                    ? that.defaultValue == null
                    : (that.defaultValue != null
                    && this.defaultValue.equals(that.defaultValue)));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 31 * key.hashCode()
                + (defaultValue != null ? defaultValue.hashCode() : 0);
    }

    @Override
    public String toString() {
        return String.format(
                "Key: '%s' , default: %s ",
                key, defaultValue);
    }
}

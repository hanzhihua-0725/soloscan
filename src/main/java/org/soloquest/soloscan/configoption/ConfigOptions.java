package org.soloquest.soloscan.configoption;

import org.soloquest.soloscan.utils.Preconditions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConfigOptions {

    public static OptionBuilder key(String key) {
        Preconditions.checkNotNull(key);
        return new OptionBuilder(key);
    }

    public static final class OptionBuilder {

        private static final Class<Map<String, String>> PROPERTIES_MAP_CLASS =
                (Class<Map<String, String>>) (Class<?>) Map.class;

        private final String key;

        OptionBuilder(String key) {
            this.key = key;
        }

        public TypedConfigOptionBuilder<Boolean> booleanType() {
            return new TypedConfigOptionBuilder<>(key, Boolean.class);
        }

        public TypedConfigOptionBuilder<Integer> intType() {
            return new TypedConfigOptionBuilder<>(key, Integer.class);
        }

        public TypedConfigOptionBuilder<Long> longType() {
            return new TypedConfigOptionBuilder<>(key, Long.class);
        }

        public TypedConfigOptionBuilder<Float> floatType() {
            return new TypedConfigOptionBuilder<>(key, Float.class);
        }

        public TypedConfigOptionBuilder<Double> doubleType() {
            return new TypedConfigOptionBuilder<>(key, Double.class);
        }

        public TypedConfigOptionBuilder<String> stringType() {
            return new TypedConfigOptionBuilder<>(key, String.class);
        }


        public <T extends Enum<T>> TypedConfigOptionBuilder<T> enumType(Class<T> enumClass) {
            return new TypedConfigOptionBuilder<>(key, enumClass);
        }

        public TypedConfigOptionBuilder<Map<String, String>> mapType() {
            return new TypedConfigOptionBuilder<>(key, PROPERTIES_MAP_CLASS);
        }

        public <T> ConfigOption<T> defaultValue(T value) {
            Preconditions.checkNotNull(value);
            return new ConfigOption<>(
                    key, value.getClass(), ConfigOption.EMPTY_DESCRIPTION, value, false);
        }

        public ConfigOption<String> noDefaultValue() {
            return new ConfigOption<>(
                    key, String.class, ConfigOption.EMPTY_DESCRIPTION, null, false);
        }
    }

    public static class TypedConfigOptionBuilder<T> {
        private final String key;
        private final Class<T> clazz;

        TypedConfigOptionBuilder(String key, Class<T> clazz) {
            this.key = key;
            this.clazz = clazz;
        }

        public ListConfigOptionBuilder<T> asList() {
            return new ListConfigOptionBuilder<>(key, clazz);
        }

        public ConfigOption<T> defaultValue(T value) {
            return new ConfigOption<>(key, clazz, ConfigOption.EMPTY_DESCRIPTION, value, false);
        }

        public ConfigOption<T> noDefaultValue() {
            return new ConfigOption<>(
                    key, clazz, ConfigOption.EMPTY_DESCRIPTION, null, false);
        }
    }

    public static class ListConfigOptionBuilder<E> {
        private final String key;
        private final Class<E> clazz;

        ListConfigOptionBuilder(String key, Class<E> clazz) {
            this.key = key;
            this.clazz = clazz;
        }

        public final ConfigOption<List<E>> defaultValues(E... values) {
            return new ConfigOption<>(
                    key, clazz, ConfigOption.EMPTY_DESCRIPTION, Arrays.asList(values), true);
        }

        public ConfigOption<List<E>> noDefaultValue() {
            return new ConfigOption<>(key, clazz, ConfigOption.EMPTY_DESCRIPTION, null, true);
        }
    }

    private ConfigOptions() {
    }
}

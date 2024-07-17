package org.soloquest.soloscan;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.soloquest.soloscan.configoption.ConfigOption;
import org.soloquest.soloscan.configoption.ConfigOptions;
import org.soloquest.soloscan.configoption.Configuration;
import org.soloquest.soloscan.utils.MiscUtils;

public class SoloscanOptions {

    public static final ConfigOption<Boolean> CONCURRENT_PROCESSS =
            ConfigOptions.key("concurrent.process")
                    .booleanType()
                    .defaultValue(false)
                    .withDescription(
                            "use concurrent process agg");
    public static final ConfigOption<Boolean> COLUMN_CASE_INSENSITIVE =
            ConfigOptions.key("column.case.insensitive")
                    .booleanType()
                    .defaultValue(false)
                    .withDescription(
                            "column case insensitive,default value is false");
    public static final ConfigOption<Boolean> GENERATE_CLASS =
            ConfigOptions.key("generate.class")
                    .booleanType()
                    .defaultValue(false)
                    .withDescription(
                            "generate.class");
    public static final ConfigOption<Boolean> USE_METHODHANDLE =
            ConfigOptions.key("use.methodhandle")
                    .booleanType()
                    .defaultValue(true)
                    .withDescription(
                            "use method handle");
    public static final ConfigOption<Boolean> SILENCE_MODE =
            ConfigOptions.key("silence.mode")
                    .booleanType()
                    .defaultValue(false)
                    .withDescription(
                            "suppress log level to error");
    public static final ConfigOption<Integer> EXECUTE_TIMEOUT_MS =
            ConfigOptions.key("execute.timeout")
                    .intType()
                    .defaultValue(0)
                    .withDescription(
                            "execute timeout,the unit is ms");
    public static final ConfigOption<String> GENERATE_CLASS_ROOT_PATH =
            ConfigOptions.key("generate.class.root.path")
                    .stringType()
                    .defaultValue(System.getProperty("java.io.tmpdir"));
    private final static Configuration configuration = Configuration.fromMap(MiscUtils.forciblyCast(System.getProperties()));
    public static Level INIT_LEVEL;

    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger("org.soloquest.soloscan");
        INIT_LEVEL = logger.getLevel();
        if (SoloscanOptions.getOption(SILENCE_MODE)) {
            logger.error("use silence mode");
            logger.setLevel(Level.ERROR);
        }else{
            logger.info("disable silence mode");
        }
    }

    public static <T> T getOption(ConfigOption<T> configOption) {
        return configuration.get(configOption);
    }

    public static void set(ConfigOption configOption, Object value) {
        set(configOption.key(), value);
    }

    public static void set(String key, Object value) {
        configuration.set(key, value);
        if (SILENCE_MODE.key().equals(key)) {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            ch.qos.logback.classic.Logger logger = loggerContext.getLogger("org.soloquest.soloscan");
            if (Boolean.parseBoolean(value.toString())) {
                logger.setLevel(ch.qos.logback.classic.Level.ERROR);
            } else {
                logger.setLevel(INIT_LEVEL);
            }
        }
    }
}

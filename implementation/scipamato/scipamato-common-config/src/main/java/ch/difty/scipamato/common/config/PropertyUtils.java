package ch.difty.scipamato.common.config;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class with static helper methods used for configuration/property
 * evaluation purposes.
 *
 * @author u.joss
 */
@Slf4j
public final class PropertyUtils {

    private PropertyUtils() {
    }

    /**
     * Derive an enum from a configuration property value.
     *
     * @param propertyValue
     *            the configured property value.
     * @param values
     *            all values of the enum, hand in {@code T.values}
     * @param defaultValue
     *            the default enum value that shall be returned if the property
     *            value does not explicitly match an enum value.
     * @param propertyKey
     *            the property key, used only for logging purposes
     * @return enum of type {@code T}
     */
    public static <T extends Enum<T>> T fromProperty(String propertyValue, T[] values, T defaultValue,
            String propertyKey) {
        if (StringUtils.isNotBlank(propertyValue)) {
            for (T s : values) {
                if (propertyValue.equalsIgnoreCase(s.name())) {
                    log.info("{}={}", propertyKey, propertyValue);
                    return s;
                }
            }
        }
        final String msg = "{} is not properly defined. Current value: '{}' - now using {} - "
                + "specify one of {} in your property configuration (e.g. application.properties).";
        log.warn(msg, propertyKey, propertyValue, defaultValue, values);
        return defaultValue;
    }

    /**
     * Derive an int value from a configuration property value.
     *
     * @param propertyValue
     *            the configured property value as string
     * @param values
     *            possible values for log in case of a warn message
     * @param defaultValue
     *            the default value that shall be returned if the property value
     *            cannot be parsed as integer
     * @param propertyKey
     *            the property key, used only for logging purposes
     * @return the int value
     */
    public static int parseInt(String propertyValue, String values, int defaultValue, String propertyKey) {
        int value = defaultValue;
        try {
            value = Integer.parseInt(propertyValue);
            log.info("{}={}", propertyKey, propertyValue);
        } catch (NumberFormatException ex) {
            final String msg = "{} is not properly defined. Current value: '{}' - now using {} - "
                    + "specify one in your property configuration (e.g. application.properties).{}";
            log.warn(msg, propertyKey, propertyValue, defaultValue, values);
        }
        return value;
    }

}

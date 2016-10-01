package ch.difty.sipamato.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class with static helper methods used for configuration/property evaluation purposes.
 *
 * @author u.joss
 */
public final class PropertyUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtils.class);

    private PropertyUtils() {
    }

    /**
     * Derive an enum from a configuration property value.
     * @param propertyValue
     *          the configured property value.
     * @param values
     *          all values of the enum, hand in <code>T.values</code>
     * @param defaultValue
     *          the default enum value that shall be returned if the property value does not explicitly match an enum value.
     * @param propertyKey
     *          the property key, used only for loggin purposes
     * @param T type of the enum
     * @return enum of type <literal>T</literal>
     */
    public static <T extends Enum<T>> T fromProperty(String propertyValue, T[] values, T defaultValue, String propertyKey) {
        if (StringUtils.isNotBlank(propertyValue)) {
            for (T s : values) {
                if (propertyValue.equalsIgnoreCase(s.name())) {
                    LOGGER.info("{}={}", propertyKey, propertyValue);
                    return s;
                }
            }
        }
        final String msg = "{} is not properly defined. Current value: '{}' - now using {} - specify one of {} in your property configuration (e.g. application.properties).";
        LOGGER.warn(msg, propertyKey, propertyValue, defaultValue, values);
        return defaultValue;
    }
}

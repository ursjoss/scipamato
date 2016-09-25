package ch.difty.sipamato.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a property value to define the AuthorParserStrategy to be used.
 *
 * @author u.joss
 */
public enum AuthorParserStrategies {
    DEFAULT;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorParserStrategies.class);

    public static AuthorParserStrategies fromProperty(String property) {
        if (StringUtils.isNotBlank(property)) {
            for (AuthorParserStrategies s : values()) {
                if (property.equals(s.name())) {
                    LOGGER.info("{}={}", ApplicationProperties.AUTHOR_PARSER_FACTORY, property);
                    return s;
                }
            }
        }
        LOGGER.warn("{} is not properly defined. Current value: '{}' - choose one of {}.", ApplicationProperties.AUTHOR_PARSER_FACTORY, property, values());
        return DEFAULT;
    }
}

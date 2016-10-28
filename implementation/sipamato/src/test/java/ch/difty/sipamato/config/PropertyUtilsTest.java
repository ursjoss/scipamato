package ch.difty.sipamato.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PropertyUtilsTest {

    enum PropertyTestEnum {
        VAL1,
        VAL2,
        DEFAULT;

        public static PropertyTestEnum fromProperty(final String propertyValue) {
            return PropertyUtils.fromProperty(propertyValue, values(), DEFAULT, "sipamato.test.property");
        }
    }

    PropertyTestEnum[] values = { PropertyTestEnum.VAL1, PropertyTestEnum.VAL2, PropertyTestEnum.DEFAULT };

    @Test
    public void fromProperty_withNullProperty_resortsToDefaultValue() {
        assertThat(PropertyUtils.fromProperty(null, values, PropertyTestEnum.DEFAULT, "propertyKey")).isEqualTo(PropertyTestEnum.DEFAULT);
    }

    @Test
    public void fromProperty() {
        assertThat(PropertyUtils.fromProperty("VAL2", values, PropertyTestEnum.DEFAULT, "propertyKey")).isEqualTo(PropertyTestEnum.VAL2);
    }

    @Test
    public void parseInt_withValidIntString_returnsParsedInt() {
        assertThat(PropertyUtils.parseInt("5", "value", 1, "propertyKey")).isEqualTo(5);
    }

    @Test
    public void parseInt_withInvValidIntString_returnsDefaultValue() {
        assertThat(PropertyUtils.parseInt("foo", "value", 1, "propertyKey")).isEqualTo(1);
    }
}

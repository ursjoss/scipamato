package ch.difty.scipamato.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PropertyUtilsTest extends FinalClassTest<PropertyUtils> {

    private static final String PROPERTY_KEY = "propertyKey";

    enum PropertyTestEnum {
        VAL1,
        VAL2,
        DEFAULT;

        public static PropertyTestEnum fromProperty(final String propertyValue) {
            return PropertyUtils.fromProperty(propertyValue, values(), DEFAULT, "scipamato.test.property");
        }
    }

    private PropertyTestEnum[] values = { PropertyTestEnum.VAL1, PropertyTestEnum.VAL2, PropertyTestEnum.DEFAULT };

    @Test
    public void fromProperty_withNullProperty_resortsToDefaultValue() {
        assertThat(PropertyUtils.fromProperty(null, values, PropertyTestEnum.DEFAULT, PROPERTY_KEY)).isEqualTo(
            PropertyTestEnum.DEFAULT);
    }

    @Test
    public void fromProperty() {
        assertThat(PropertyUtils.fromProperty("VAL2", values, PropertyTestEnum.DEFAULT, PROPERTY_KEY)).isEqualTo(
            PropertyTestEnum.VAL2);
    }

    @Test
    public void parseInt_withValidIntString_returnsParsedInt() {
        assertThat(PropertyUtils.parseInt("5", "value", 1, PROPERTY_KEY)).isEqualTo(5);
    }

    @Test
    public void parseInt_withInvValidIntString_returnsDefaultValue() {
        assertThat(PropertyUtils.parseInt("foo", "value", 1, PROPERTY_KEY)).isEqualTo(1);
    }

}

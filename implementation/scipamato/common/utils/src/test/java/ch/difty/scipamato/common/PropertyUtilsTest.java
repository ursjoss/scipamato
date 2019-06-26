package ch.difty.scipamato.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PropertyUtilsTest implements FinalClassTest<PropertyUtils> {

    private static final String PROPERTY_KEY = "propertyKey";

    enum PropertyTestEnum {
        VAL1,
        VAL2,
        DEFAULT;

        public static PropertyTestEnum fromProperty(final String propertyValue) {
            return PropertyUtils.fromProperty(propertyValue, values(), DEFAULT, "scipamato.test.property");
        }
    }

    private final PropertyTestEnum[] values = { PropertyTestEnum.VAL1, PropertyTestEnum.VAL2,
        PropertyTestEnum.DEFAULT };

    @Test
    void fromProperty_withNullProperty_resortsToDefaultValue() {
        assertThat(PropertyUtils.fromProperty(null, values, PropertyTestEnum.DEFAULT, PROPERTY_KEY)).isEqualTo(
            PropertyTestEnum.DEFAULT);
    }

    @Test
    void fromProperty_witValues_retunsValue() {
        assertThat(PropertyUtils.fromProperty("VAL2", values, PropertyTestEnum.DEFAULT, PROPERTY_KEY)).isEqualTo(
            PropertyTestEnum.VAL2);
    }

    @Test
    void fromProperty_withoutValues_returnsDefault() {
        assertThat(PropertyUtils.fromProperty("VAL2", new PropertyTestEnum[] {}, PropertyTestEnum.DEFAULT,
            PROPERTY_KEY)).isEqualTo(PropertyTestEnum.DEFAULT);
    }

    @Test
    void parseInt_withValidIntString_returnsParsedInt() {
        assertThat(PropertyUtils.parseInt("5", "value", 1, PROPERTY_KEY)).isEqualTo(5);
    }

    @Test
    void parseInt_withInvValidIntString_returnsDefaultValue() {
        assertThat(PropertyUtils.parseInt("foo", "value", 1, PROPERTY_KEY)).isEqualTo(1);
    }

}

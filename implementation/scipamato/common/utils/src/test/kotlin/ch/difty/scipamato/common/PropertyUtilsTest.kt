package ch.difty.scipamato.common

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

private const val PROPERTY_KEY = "propertyKey"

internal class PropertyUtilsTest : FinalClassTest<PropertyUtils> {

    private val values = arrayOf(PropertyTestEnum.VAL1, PropertyTestEnum.VAL2, PropertyTestEnum.DEFAULT)

    private enum class PropertyTestEnum { VAL1, VAL2, DEFAULT }

    @Test
    fun fromProperty_withNullProperty_resortsToDefaultValue() {
        assertThat(PropertyUtils.fromProperty(null, values, PropertyTestEnum.DEFAULT, PROPERTY_KEY)).isEqualTo(
                PropertyTestEnum.DEFAULT)
    }

    @Test
    fun fromProperty_witValues_retunsValue() {
        assertThat(PropertyUtils.fromProperty("VAL2", values, PropertyTestEnum.DEFAULT, PROPERTY_KEY)).isEqualTo(
                PropertyTestEnum.VAL2)
    }

    @Test
    fun fromProperty_withoutValues_returnsDefault() {
        assertThat(PropertyUtils.fromProperty("VAL2", arrayOf(), PropertyTestEnum.DEFAULT,
                PROPERTY_KEY)).isEqualTo(PropertyTestEnum.DEFAULT)
    }

    @Test
    fun parseInt_withValidIntString_returnsParsedInt() {
        assertThat(PropertyUtils.parseInt("5", "value", 1, PROPERTY_KEY)).isEqualTo(5)
    }

    @Test
    fun parseInt_withInvValidIntString_returnsDefaultValue() {
        assertThat(PropertyUtils.parseInt("foo", "value", 1, PROPERTY_KEY)).isEqualTo(1)
    }

}

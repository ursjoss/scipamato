package ch.difty.scipamato.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.sql.Timestamp

private const val PROPERTY_KEY = "propertyKey"

internal class UtilsTest {

    //region:toLocalDateTime

    @Test
    fun tsOf_withNonNullDate() {
        val ts = Timestamp.valueOf("2017-01-03 13:55:12.123")
        assertThat(ts.toLocalDateTime().toTimestamp()).isEqualTo(ts)
    }

    //endregion


    //region:asProperty

    private val values = arrayOf(PropertyTestEnum.VAL1, PropertyTestEnum.VAL2, PropertyTestEnum.DEFAULT)

    private enum class PropertyTestEnum { VAL1, VAL2, DEFAULT }

    @Test
    fun fromProperty_witValues_returnsValue() {
        assertThat("VAL2".asProperty(values, PropertyTestEnum.DEFAULT, PROPERTY_KEY)).isEqualTo(
                PropertyTestEnum.VAL2)
    }

    @Test
    fun fromProperty_withoutValues_returnsDefault() {
        assertThat("VAL2".asProperty(arrayOf(), PropertyTestEnum.DEFAULT, PROPERTY_KEY)).isEqualTo(PropertyTestEnum.DEFAULT)
    }

    //endregion
}

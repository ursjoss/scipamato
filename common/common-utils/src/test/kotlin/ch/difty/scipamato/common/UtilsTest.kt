package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import java.sql.Timestamp

private const val PROPERTY_KEY = "propertyKey"

internal class UtilsTest {

    //region:toLocalDateTime

    @Test
    fun tsOf_withNonNullDate() {
        val ts = Timestamp.valueOf("2017-01-03 13:55:12.123")
        ts.toLocalDateTime().toTimestamp() shouldBeEqualTo ts
    }

    //endregion

    //region:asProperty

    private val values = arrayOf(PropertyTestEnum.VAL1, PropertyTestEnum.VAL2, PropertyTestEnum.DEFAULT)

    private enum class PropertyTestEnum { VAL1, VAL2, DEFAULT }

    @Test
    fun asProperty_witValues_returnsValue() {
        "VAL2".asProperty(values, PropertyTestEnum.DEFAULT, PROPERTY_KEY) shouldBeEqualTo PropertyTestEnum.VAL2
    }

    @Test
    fun asProperty_withoutValues_returnsDefault() {
        "VAL2".asProperty(arrayOf(), PropertyTestEnum.DEFAULT, PROPERTY_KEY) shouldBeEqualTo PropertyTestEnum.DEFAULT
    }

    @Test
    fun asProperty_withBlankReceiver_returnsDefault() {
        "".asProperty(arrayOf(), PropertyTestEnum.DEFAULT, PROPERTY_KEY) shouldBeEqualTo PropertyTestEnum.DEFAULT
    }

    //endregion
}

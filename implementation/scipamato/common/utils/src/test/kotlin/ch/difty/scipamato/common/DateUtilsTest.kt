package ch.difty.scipamato.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.sql.Timestamp

internal class DateUtilsTest : FinalClassTest<DateUtils> {

    @Test
    fun tsOf_withNullDate() {
        assertThat(DateUtils.tsOf(null)).isNull()
    }

    @Test
    fun tsOf_withNonNullDate() {
        val ts = Timestamp.valueOf("2017-01-03 13:55:12.123")
        assertThat(DateUtils.tsOf(ts.toLocalDateTime())).isEqualTo(ts)
    }

}

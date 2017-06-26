package ch.difty.scipamato.lib;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;

import org.junit.Test;

import ch.difty.scipamato.FinalClassTest;

public class DateUtilsTest extends FinalClassTest<DateUtils> {

    @Test
    public void tsOf_withNullDate() {
        assertThat(DateUtils.tsOf(null)).isNull();
    }

    @Test
    public void tsOf_withNonNullDate() {
        Timestamp ts = Timestamp.valueOf("2017-01-03 13:55:12.123");
        assertThat(DateUtils.tsOf(ts.toLocalDateTime())).isEqualTo(ts);
    }

}

package ch.difty.scipamato.lib;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class FrozenDateTimeServiceTest {

    private final DateTimeService service = new FrozenDateTimeService();

    @Test
    public void gettingDateTime() {
        assertThat(service.getCurrentDateTime().toString()).isEqualTo("2016-12-09T06:02:13");
    }

    @Test
    public void gettingTimestamp() {
        assertThat(service.getCurrentTimestamp().toString()).isEqualTo("2016-12-09 06:02:13.0");
    }

    @Test
    public void gettingCurrentDate() {
        assertThat(service.getCurrentDate().toString()).isEqualTo("2016-12-09");
    }
}

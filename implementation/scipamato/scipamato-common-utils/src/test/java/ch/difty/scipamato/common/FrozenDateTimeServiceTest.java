package ch.difty.scipamato.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FrozenDateTimeServiceTest {

    private final DateTimeService service = new FrozenDateTimeService();

    @Test
    void gettingDateTime() {
        assertThat(service
            .getCurrentDateTime()
            .toString()).isEqualTo("2016-12-09T06:02:13");
    }

    @Test
    void gettingTimestamp() {
        assertThat(service
            .getCurrentTimestamp()
            .toString()).isEqualTo("2016-12-09 06:02:13.0");
    }

    @Test
    void gettingCurrentDate() {
        assertThat(service
            .getCurrentDate()
            .toString()).isEqualTo("2016-12-09");
    }
}

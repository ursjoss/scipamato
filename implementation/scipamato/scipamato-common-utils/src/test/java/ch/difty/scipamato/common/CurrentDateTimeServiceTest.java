package ch.difty.scipamato.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class CurrentDateTimeServiceTest {

    private final DateTimeService dts = new CurrentDateTimeService();

    @Test
    void gettingCurrentDateTime() {
        LocalDateTime snapshot = LocalDateTime.now();
        LocalDateTime ldt = dts.getCurrentDateTime();
        assertThat(ldt).isBetween(snapshot, snapshot.plusSeconds(1));
    }

    @Test
    void gettingCurrentDate() {
        LocalDate snapshot = LocalDate.now();
        LocalDate ld = dts.getCurrentDate();
        assertThat(ld).isEqualTo(snapshot);
    }

    @Test
    void gettingCurrentTimeStamp() {
        LocalDateTime snapshot = LocalDateTime.now();
        LocalDateTime ldt = dts
            .getCurrentTimestamp()
            .toLocalDateTime();
        assertThat(ldt).isBetween(snapshot.minusSeconds(1), snapshot.plusSeconds(1));
    }

}

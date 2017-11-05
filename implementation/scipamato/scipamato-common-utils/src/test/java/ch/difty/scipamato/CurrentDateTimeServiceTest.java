package ch.difty.scipamato;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

public class CurrentDateTimeServiceTest {

    private final DateTimeService dts = new CurrentDateTimeService();

    @Test
    public void gettingCurrentDateTime() {
        LocalDateTime snapshot = LocalDateTime.now();
        LocalDateTime ldt = dts.getCurrentDateTime();
        assertThat(ldt).isBetween(snapshot, snapshot.plusSeconds(1));
    }

    @Test
    public void gettingCurrentDate() {
        LocalDate snapshot = LocalDate.now();
        LocalDate ld = dts.getCurrentDate();
        assertThat(ld).isEqualTo(snapshot);
    }

    @Test
    public void gettingCurrentTimeStamp() {
        LocalDateTime snapshot = LocalDateTime.now();
        LocalDateTime ldt = dts.getCurrentTimestamp().toLocalDateTime();
        assertThat(ldt).isBetween(snapshot, snapshot.plusSeconds(1));
    }

}

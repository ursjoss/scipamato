package ch.difty.sipamato.lib;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void gettingCurrentTimeStamp() {
        LocalDateTime snapshot = LocalDateTime.now();
        LocalDateTime ldt = dts.getCurrentTimestamp().toLocalDateTime();
        assertThat(ldt).isBetween(snapshot, snapshot.plusSeconds(1));
    }
}

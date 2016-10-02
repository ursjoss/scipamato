package ch.difty.sipamato.lib;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class CurrentDateTimeService implements DateTimeService {

    @Override
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    @Override
    public Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}

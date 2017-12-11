package ch.difty.scipamato.common;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

/**
 * Implementation of {@link DateTimeService} actually providing access to the system clock.
 * To be used in production.
 *
 * @author u.joss
 */
@Component
public class CurrentDateTimeService implements DateTimeService {

    /** {@inheritDoc} */
    @Override
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /** {@inheritDoc} */
    @Override
    public Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /** {@inheritDoc} */
    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}

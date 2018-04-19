package ch.difty.scipamato.common;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Implementation of {@link DateTimeService} actually providing access to the
 * system clock. To be used in production.
 *
 * @author u.joss
 */
class CurrentDateTimeService implements DateTimeService {

    @Override
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    @Override
    public Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}

package ch.difty.scipamato.common;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link DateTimeService} constantly returning a frozen
 * moment. This is the implementation to be used in test context.
 *
 * @author u.joss
 */
@Component
@Primary
public class FrozenDateTimeService implements DateTimeService {

    private static final LocalDateTime FROZEN = LocalDateTime.parse("2016-12-09T06:02:13");

    /** {@inheritDoc} */
    @Override
    public LocalDateTime getCurrentDateTime() {
        return FROZEN;
    }

    /** {@inheritDoc} */
    @Override
    public Timestamp getCurrentTimestamp() {
        return Timestamp.valueOf(FROZEN);
    }

    /** {@inheritDoc} */
    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.from(FROZEN);
    }

}

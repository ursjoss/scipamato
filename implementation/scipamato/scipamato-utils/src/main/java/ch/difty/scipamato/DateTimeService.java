package ch.difty.scipamato;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Implementations provide access to date and time.
 * It's useful to have this as service in order to be mockable.
 *
 * @author u.joss
 */
public interface DateTimeService {

    /**
     * @return now as {@link LocalDateTime}
     */
    LocalDateTime getCurrentDateTime();

    /**
     * @return today as {@link LocalDate}
     */
    LocalDate getCurrentDate();

    /**
     * @return now as {@link Timestamp}
     */
    Timestamp getCurrentTimestamp();

}
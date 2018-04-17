package ch.difty.scipamato.common;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public final class DateUtils {

    private DateUtils() {
    }

    /**
     * Converts a {@link LocalDateTime} into a {@link Timestamp}.
     *
     * @param ldt
     *            the localDateTime to convert
     * @return the timestamp - or null if ldt is null
     */
    public static Timestamp tsOf(final LocalDateTime ldt) {
        return ldt != null ? Timestamp.valueOf(ldt) : null;
    }
}

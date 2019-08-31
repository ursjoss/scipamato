package ch.difty.scipamato.core.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.DSLContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.entity.User;

/**
 * Generic base class for Repositories, providing protected accessor methods for the dslContext and the dateTimeService.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractRepo {

    private final DSLContext      dsl;
    private final DateTimeService dateTimeService;

    /**
     * @param dsl
     *     the {@link DSLContext}
     * @param dateTimeService
     *     the {@link DateTimeService} providing access to the system time
     */
    protected AbstractRepo(DSLContext dsl, DateTimeService dateTimeService) {
        this.dsl = AssertAs.INSTANCE.notNull(dsl, "dsl");
        this.dateTimeService = AssertAs.INSTANCE.notNull(dateTimeService, "dateTimeService");
    }

    protected DSLContext getDsl() {
        return dsl;
    }

    protected DateTimeService getDateTimeService() {
        return dateTimeService;
    }

    /**
     * @return the current {@link User}
     */
    protected User getActiveUser() {
        final Authentication auth = getAuthentication();
        if (auth != null) {
            return (User) auth.getPrincipal();
        } else {
            return User.NO_USER;
        }
    }

    Authentication getAuthentication() {
        return SecurityContextHolder
            .getContext()
            .getAuthentication();
    }

    /**
     * @return the id of the currently active {@link User}
     */
    protected Integer getUserId() {
        return getActiveUser().getId();
    }

    /**
     * @return the current date as {@link Timestamp}
     */
    protected Timestamp getTs() {
        return getDateTimeService().getCurrentTimestamp();
    }

    /**
     * @return the current date as {@link Timestamp}
     */
    protected LocalDateTime now() {
        return getDateTimeService().getCurrentDateTime();
    }

}

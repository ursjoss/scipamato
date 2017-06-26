package ch.difty.scipamato.auth;

/**
 * Definition of the {@link Role} names.
 *
 * They are used in two places, that should be consistent:
 *
 * <ol>
 * <li> as keys in the {@link Role} enum defining the roles. </li>
 * <li> for authorization on page level (where the enum can't be used) </li>
 * </ol>
 *
 * An example of how to authorize on page level:<p>
 *
 * {@literal @AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })}
 *
 * @author u.joss
 */
public final class Roles {

    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String VIEWER = "ROLE_VIEWER";

    private Roles() {
    }
}

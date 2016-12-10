package ch.difty.sipamato.auth;

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
 * An example of how to authorize on page level:<p/>
 *
 * <code>@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })</code
 *
 * @author u.joss
 */
public interface Roles {

    String USER = "ROLE_USER";
    String ADMIN = "ROLE_ADMIN";
    String VIEWER = "ROLE_VIEWER";
}

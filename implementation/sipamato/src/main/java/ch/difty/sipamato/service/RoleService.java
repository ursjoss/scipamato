package ch.difty.sipamato.service;

import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.persistance.jooq.role.RoleFilter;

/**
 * The {@link RoleService} interface - defining {@link Role} specific service methods.
 *
 * @author u.joss
 */
public interface RoleService extends EntityService<Integer, Role, RoleFilter> {

}

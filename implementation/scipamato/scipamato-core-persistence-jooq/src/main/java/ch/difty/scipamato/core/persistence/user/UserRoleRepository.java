package ch.difty.scipamato.core.persistence.user;

import java.util.Set;

import ch.difty.scipamato.core.auth.Role;

public interface UserRoleRepository {

    /**
     * Finds all {@link Role}s assigned to user with the provided id.
     *
     * @param userId
     *     the id of the user to find roles for
     * @return list of {@link Role}s
     */
    Set<Role> findRolesForUser(Integer userId);

    /**
     * Adds all {@link Role}s to the user with the provided id. If any {@link Role}
     * has already been assigned, the insert will be ignored. If the user has more
     * {@link Role}s persisted than what's in the provided list, those are not
     * deleted and are also be returned in the returned list.
     *
     * @param userId
     *     the id of the user to assign the {@link Role}s to.
     * @param roles
     *     the {@link Role}s that need to be assigned to the user.
     * @return all roles assigned to the user
     */
    Set<Role> addNewUserRolesOutOf(Integer userId, Set<Role> roles);

    /**
     * Deletes any {@link Role}s the user might have assigned *except* those in the
     * provided list.
     *
     * @param userId
     *     the id of the user
     * @param roleIds
     *     the role ids that shall *not* be deleted
     */
    void deleteAllRolesExcept(Integer userId, Set<Integer> roleIds);

}

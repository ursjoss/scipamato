package ch.difty.sipamato.persistance.jooq.role;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.service.RoleService;

/**
 * jOOQ specific implementation of the {@link RoleService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqRoleService implements RoleService {

    private static final long serialVersionUID = 1L;

    private RoleRepository repo;

    @Autowired
    public void setRepository(RoleRepository repo) {
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Role> findById(Integer id) {
        return Optional.ofNullable(repo.findById(id));
    }

    /** {@inheritDoc} */
    @Override
    public List<Role> findByFilter(RoleFilter filter, Pageable pageable) {
        return repo.findByFilter(filter, pageable).getContent();
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(RoleFilter filter) {
        return repo.countByFilter(filter);
    }

    /** {@inhericDoc} */
    @Override
    public Role saveOrUpdate(Role role) {
        if (role.getId() == null) {
            return repo.add(role);
        } else {
            return repo.update(role);
        }
    }

}

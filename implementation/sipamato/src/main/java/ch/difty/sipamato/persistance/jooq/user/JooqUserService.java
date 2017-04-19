package ch.difty.sipamato.persistance.jooq.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.paging.Pageable;
import ch.difty.sipamato.service.UserService;

/**
 * jOOQ specific implementation of the {@link UserService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqUserService implements UserService {

    private static final long serialVersionUID = 1L;

    private UserRepository repo;

    @Autowired
    public void setRepository(UserRepository repo) {
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(repo.findById(id));
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findPageByFilter(UserFilter filter, Pageable pageable) {
        return repo.findPageByFilter(filter, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public int countByFilter(UserFilter filter) {
        return repo.countByFilter(filter);
    }

    /** {@inhericDoc} */
    @Override
    public User saveOrUpdate(User user) {
        if (user.getId() == null) {
            return repo.add(user);
        } else {
            return repo.update(user);
        }
    }

    /** {@inhericDoc} */
    @Override
    public Optional<User> findByUserName(String userName) {
        return Optional.ofNullable(repo.findByUserName(userName));
    }

    /** {@inhericDoc} */
    @Override
    public void remove(User entity) {
        if (entity != null && entity.getId() != null) {
            repo.delete(entity.getId());
        }
    }

}

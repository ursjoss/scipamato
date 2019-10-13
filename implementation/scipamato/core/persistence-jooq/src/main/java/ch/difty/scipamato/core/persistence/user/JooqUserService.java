package ch.difty.scipamato.core.persistence.user;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;
import ch.difty.scipamato.core.persistence.UserRepository;
import ch.difty.scipamato.core.persistence.UserService;

/**
 * jOOQ specific implementation of the {@link UserService} interface.
 * <p>
 * Note: This service is deliberately not extending JooqEntityService as that
 * depending on on this service itself.
 *
 * @author u.joss
 */
@Service
public class JooqUserService implements UserService {

    private final UserRepository repo;

    private final PasswordEncoder passwordEncoder;

    public JooqUserService(@NotNull final UserRepository repo, @Lazy @NotNull final PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @NotNull
    @Override
    public Optional<User> findById(@NotNull Integer id) {
        return Optional.ofNullable(repo.findById(id));
    }

    @NotNull
    @Override
    public List<User> findPageByFilter(@Nullable UserFilter filter, @NotNull PaginationContext paginationContext) {
        return repo.findPageByFilter(filter, paginationContext);
    }

    @Override
    public int countByFilter(@Nullable UserFilter filter) {
        return repo.countByFilter(filter);
    }

    @Nullable
    @Override
    public User saveOrUpdate(@NotNull User user) {
        final String password = user.getPassword();
        if (password != null)
            user.setPassword(passwordEncoder.encode(password));
        if (user.getId() == null)
            return repo.add(user);
        else
            return repo.update(user);
    }

    @NotNull
    @Override
    public Optional<User> findByUserName(@Nullable String userName) {
        if (userName == null)
            return Optional.empty();
        return Optional.ofNullable(repo.findByUserName(userName));
    }

    @Override
    public void remove(@Nullable User entity) {
        if (entity != null && entity.getId() != null)
            repo.delete(entity.getId(), entity.getVersion());
    }

    @NotNull
    @Override
    public List<Integer> findPageOfIdsByFilter(@Nullable final UserFilter filter, @NotNull final PaginationContext paginationContext) {
        return repo.findPageOfIdsByFilter(filter, paginationContext);
    }
}

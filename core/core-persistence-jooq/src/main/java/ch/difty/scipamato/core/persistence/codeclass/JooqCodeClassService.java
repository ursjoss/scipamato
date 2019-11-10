package ch.difty.scipamato.core.persistence.codeclass;

import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.codeclass.JooqCodeClassLikeService;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition;
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter;
import ch.difty.scipamato.core.persistence.CodeClassService;

/**
 * jOOQ specific implementation of the {@link CodeClassService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqCodeClassService extends JooqCodeClassLikeService<CodeClass, CodeClassRepository>
    implements CodeClassService {

    public JooqCodeClassService(@NotNull final CodeClassRepository codeClassRepository) {
        super(codeClassRepository);
    }

    @NotNull
    @Override
    public List<CodeClassDefinition> findPageOfCodeClassDefinitions(@Nullable final CodeClassFilter filter,
        @NotNull final PaginationContext paginationContext) {
        return getRepo().findPageOfCodeClassDefinitions(filter, paginationContext);
    }

    @NotNull
    @Override
    public Iterator<CodeClassDefinition> findPageOfEntityDefinitions(@Nullable final CodeClassFilter filter,
        @NotNull final PaginationContext paginationContext) {
        return findPageOfCodeClassDefinitions(filter, paginationContext).iterator();
    }

    @Override
    public int countByFilter(@Nullable final CodeClassFilter filter) {
        return getRepo().countByFilter(filter);
    }

    @NotNull
    @Override
    public CodeClassDefinition newUnpersistedCodeClassDefinition() {
        return getRepo().newUnpersistedCodeClassDefinition();
    }

    @NotNull
    @Override
    public CodeClassDefinition saveOrUpdate(@NotNull final CodeClassDefinition entity) {
        return getRepo().saveOrUpdate(entity);
    }

    @Nullable
    @Override
    public CodeClassDefinition delete(@NotNull final Integer id, final int version) {
        return getRepo().delete(id, version);
    }
}

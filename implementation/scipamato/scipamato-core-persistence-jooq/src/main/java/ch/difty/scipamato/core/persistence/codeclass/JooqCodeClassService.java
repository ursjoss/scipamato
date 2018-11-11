package ch.difty.scipamato.core.persistence.codeclass;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.codeclass.JooqCodeClassLikeService;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.entity.code_class.CodeClassFilter;
import ch.difty.scipamato.core.persistence.CodeClassService;

/**
 * jOOQ specific implementation of the {@link CodeClassService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqCodeClassService extends JooqCodeClassLikeService<CodeClass, CodeClassRepository>
    implements CodeClassService {

    public JooqCodeClassService(final CodeClassRepository codeClassRepository) {
        super(codeClassRepository);
    }

    @Override
    public List<CodeClassDefinition> findPageOfCodeClassDefinitions(final CodeClassFilter filter,
        final PaginationContext paginationContext) {
        return getRepo().findPageOfCodeClassDefinitions(filter, paginationContext);
    }

    @Override
    public Iterator<CodeClassDefinition> findPageOfEntityDefinitions(final CodeClassFilter filter,
        final PaginationContext paginationContext) {
        return findPageOfCodeClassDefinitions(filter, paginationContext).iterator();
    }

    @Override
    public int countByFilter(final CodeClassFilter filter) {
        return getRepo().countByFilter(filter);
    }

    @Override
    public CodeClassDefinition newUnpersistedCodeClassDefinition() {
        return getRepo().newUnpersistedCodeClassDefinition();
    }

    @Override
    public CodeClassDefinition saveOrUpdate(final CodeClassDefinition entity) {
        return getRepo().saveOrUpdate(entity);
    }

    @Override
    public CodeClassDefinition delete(final Integer id, final int version) {
        return getRepo().delete(id, version);
    }

}

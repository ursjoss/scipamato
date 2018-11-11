package ch.difty.scipamato.core.persistence.code;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.common.persistence.code.JooqCodeLikeService;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.persistence.CodeService;

/**
 * jOOQ specific implementation of the {@link CodeService} interface.
 *
 * @author u.joss
 */
@Service
public class JooqCodeService extends JooqCodeLikeService<Code, CodeRepository> implements CodeService {

    public JooqCodeService(final CodeRepository codeRepository) {
        super(codeRepository);
    }

    @Override
    public List<CodeDefinition> findPageOfCodeDefinitions(final CodeFilter filter,
        final PaginationContext paginationContext) {
        return getRepo().findPageOfCodeDefinitions(filter, paginationContext);
    }

    @Override
    public Iterator<CodeDefinition> findPageOfEntityDefinitions(final CodeFilter filter,
        final PaginationContext paginationContext) {
        return findPageOfCodeDefinitions(filter, paginationContext).iterator();
    }

    @Override
    public int countByFilter(final CodeFilter filter) {
        return getRepo().countByFilter(filter);
    }

    @Override
    public CodeDefinition newUnpersistedCodeDefinition() {
        return getRepo().newUnpersistedCodeDefinition();
    }

    @Override
    public CodeDefinition saveOrUpdate(final CodeDefinition entity) {
        return getRepo().saveOrUpdate(entity);
    }

    @Override
    public CodeDefinition delete(final String code, final int version) {
        return getRepo().delete(code, version);
    }

    @Override
    public CodeClass getCodeClass1(final String langCode) {
        return getRepo().getCodeClass1(langCode);
    }

}

package ch.difty.scipamato.core.persistence.code;

import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    public JooqCodeService(@NotNull final CodeRepository codeRepository) {
        super(codeRepository);
    }

    @NotNull
    @Override
    public List<CodeDefinition> findPageOfCodeDefinitions(@Nullable final CodeFilter filter,
        @NotNull final PaginationContext paginationContext) {
        return getRepo().findPageOfCodeDefinitions(filter, paginationContext);
    }

    @NotNull
    @Override
    public Iterator<CodeDefinition> findPageOfEntityDefinitions(@Nullable final CodeFilter filter,
        @NotNull final PaginationContext paginationContext) {
        return findPageOfCodeDefinitions(filter, paginationContext).iterator();
    }

    @Override
    public int countByFilter(@Nullable final CodeFilter filter) {
        return getRepo().countByFilter(filter);
    }

    @NotNull
    @Override
    public CodeDefinition newUnpersistedCodeDefinition() {
        return getRepo().newUnpersistedCodeDefinition();
    }

    @Nullable
    @Override
    public CodeDefinition saveOrUpdate(@NotNull final CodeDefinition entity) {
        return getRepo().saveOrUpdate(entity);
    }

    @Nullable
    @Override
    public CodeDefinition delete(@NotNull final String code, final int version) {
        return getRepo().delete(code, version);
    }

    @NotNull
    @Override
    public CodeClass getCodeClass1(@NotNull final String langCode) {
        return getRepo().getCodeClass1(langCode);
    }
}

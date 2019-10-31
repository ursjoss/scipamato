package ch.difty.scipamato.common.persistence.code;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.CodeLike;
import ch.difty.scipamato.common.persistence.CodeLikeService;

/**
 * Generic implementation of the {@link CodeLikeService} interface.
 *
 * @param <T>
 *     code like classes extending {@link CodeLike}
 * @param <R>
 *     repositories implementing {@link CodeLikeRepository}
 * @author u.joss
 */
public abstract class JooqCodeLikeService<T extends CodeLike, R extends CodeLikeRepository<T>>
    implements CodeLikeService<T> {

    private final R repo;

    public JooqCodeLikeService(@NotNull final R repo) {
        this.repo = repo;
    }

    @NotNull
    protected R getRepo() {
        return repo;
    }

    @NotNull
    @Override
    public List<T> findCodesOfClass(@NotNull final CodeClassId codeClassId, @NotNull final String languageCode) {
        return repo.findCodesOfClass(codeClassId, languageCode);
    }

}

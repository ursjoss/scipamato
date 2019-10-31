package ch.difty.scipamato.common.persistence.codeclass;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.CodeClassLike;
import ch.difty.scipamato.common.persistence.CodeClassLikeService;

/**
 * Generic implementation of the {@link CodeClassLikeRepository}. Can be used
 * for concrete implementations in the core or public modules for code classes.
 *
 * @param <T>
 *     code classes, concrete implementations of {@link CodeClassLike}
 * @param <R>
 *     code class repository, concrete implementations of
 *     {@link CodeClassLikeRepository}
 * @author u.joss
 */
public abstract class JooqCodeClassLikeService<T extends CodeClassLike, R extends CodeClassLikeRepository<T>>
    implements CodeClassLikeService<T> {

    private final R repo;

    public JooqCodeClassLikeService(@NotNull final R repo) {
        this.repo = repo;
    }

    @NotNull
    protected R getRepo() {
        return repo;
    }

    @NotNull
    @Override
    public List<T> find(@NotNull final String languageCode) {
        return repo.find(languageCode);
    }

}

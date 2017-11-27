package ch.difty.scipamato.persistence.codeclass;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.entity.CodeClassLike;
import ch.difty.scipamato.persistence.CodeClassLikeService;

/**
 * Generic implementation of the {@link CodeClassLikeRepository}.
 * Can be used for concrete implementations in the core or public modules for code classes.
 *
 * @author u.joss
 *
 * @param <T> code classes, concrete implementations of {@link CodeClassLike} 
 * @param <R> code class repository, concrete implementations of {@link CodeClassLikeRepository}
 */
public abstract class JooqCodeClassLikeService<T extends CodeClassLike, R extends CodeClassLikeRepository<T>> implements CodeClassLikeService<T> {

    private static final long serialVersionUID = 1L;

    private R repo;

    @Autowired
    public void setRepository(final R repo) {
        this.repo = repo;
    }

    @Override
    public List<T> find(final String languageCode) {
        return repo.find(languageCode);
    }

}

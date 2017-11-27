package ch.difty.scipamato.persistence.code;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.entity.CodeLike;
import ch.difty.scipamato.persistence.CodeLikeService;

/**
 * Generic implementation of the {@link CodeLikeService} interface.
 *
 * @author u.joss
 *
 * @param <T> code like classes extending {@link CodeLike}
 * @param <R> repositories implementing {@link CodeLikeRepository}
 */
public abstract class JooqCodeLikeService<T extends CodeLike, R extends CodeLikeRepository<T>> implements CodeLikeService<T> {

    private static final long serialVersionUID = 1L;

    private R repo;

    @Autowired
    public void setRepository(final R repo) {
        this.repo = repo;
    }

    @Override
    public List<T> findCodesOfClass(CodeClassId codeClassId, String languageCode) {
        return repo.findCodesOfClass(codeClassId, languageCode);
    }

}

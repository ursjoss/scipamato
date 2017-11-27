package ch.difty.scipamato.persistence.codeclass;

import java.util.List;

import ch.difty.scipamato.entity.CodeClassLike;

/**
 * Generic interface for code class like entities of type {@code T}.
 *
 * @author u.joss
 *
 * @param <T> code classes of type {@code T}, extending {@link CodeClassLike}
 */
public interface CodeClassLikeRepository<T extends CodeClassLike> {

    /**
     * Find the localized CodeClasses of type {@code T}
     *
     * @param languageCode
     * @return a list of code classes
     */
    List<T> find(String languageCode);
}

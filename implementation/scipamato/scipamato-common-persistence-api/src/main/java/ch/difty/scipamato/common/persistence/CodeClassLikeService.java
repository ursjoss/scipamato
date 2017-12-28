package ch.difty.scipamato.common.persistence;

import java.io.Serializable;
import java.util.List;

import ch.difty.scipamato.common.entity.CodeClassLike;

/**
 * Generic service interface of code class like classes, i.e. for code classes
 * in core vs public.
 *
 * @author u.joss
 * @param <T>
 *            CodeClass like entities
 */
public interface CodeClassLikeService<T extends CodeClassLike> extends Serializable {

    /**
     * Find all code classes of Type {@code T} localized in language with the
     * provided languageCode
     *
     * @param languageCode
     * @return a list of code classes of Type {@code T}
     */
    List<T> find(String languageCode);
}

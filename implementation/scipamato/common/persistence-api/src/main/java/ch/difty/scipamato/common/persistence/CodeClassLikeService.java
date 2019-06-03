package ch.difty.scipamato.common.persistence;

import java.util.List;

import ch.difty.scipamato.common.entity.CodeClassLike;

/**
 * Generic service interface of code class like classes, i.e. for code classes
 * in core vs public.
 *
 * @param <T>
 *     CodeClass like entities
 * @author u.joss
 */
public interface CodeClassLikeService<T extends CodeClassLike> {

    /**
     * Find all code classes of Type {@code T} localized in language with the
     * provided languageCode
     *
     * @param languageCode
     *     language code, e.g. 'en' or 'de'
     * @return a list of code classes of Type {@code T}
     */
    List<T> find(String languageCode);
}

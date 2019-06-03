package ch.difty.scipamato.common.persistence.code;

import java.util.List;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.CodeLike;

/**
 * Generic interface for code like entities of type {@code T}.
 *
 * @param <T>
 *     codes of type {@code T}, extending {@link CodeLike}
 * @author u.joss
 */
public interface CodeLikeRepository<T extends CodeLike> {

    /**
     * Find all codes of type {@code T} of the specified {@link CodeClassId}
     * localized in language with the provided languageCode
     *
     * @param codeClassId
     *     the id of the code class for which to find the codes
     * @param languageCode
     *     the language code, e.g. 'en' or 'de'
     * @return a list of codes implementing {@code T}
     */
    List<T> findCodesOfClass(CodeClassId codeClassId, String languageCode);
}

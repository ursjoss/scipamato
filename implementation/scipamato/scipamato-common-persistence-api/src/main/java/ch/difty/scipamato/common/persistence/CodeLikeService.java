package ch.difty.scipamato.common.persistence;

import java.util.List;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.CodeLike;

public interface CodeLikeService<T extends CodeLike> {

    /**
     * Find all codes of type {@code T} of the specified {@link CodeClassId}
     * localized in language with the provided languageCode
     *
     * @param codeClassId
     *          the id of the code class to find the codes for.
     * @param languageCode
     *          the language code, e.g. 'en' or 'de'.
     * @return a list of codes
     */
    List<T> findCodesOfClass(CodeClassId codeClassId, String languageCode);

}
